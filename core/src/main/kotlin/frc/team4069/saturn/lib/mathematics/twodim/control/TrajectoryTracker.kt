package frc.team4069.saturn.lib.mathematics.twodim.control

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rectangle2d
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.TrajectoryIterator
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedEntry
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedTrajectory
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.Trajectory
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TrajectorySamplePoint
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.AngularAcceleration
import frc.team4069.saturn.lib.mathematics.units.conversions.AngularVelocity
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import frc.team4069.saturn.lib.util.DeltaTime

abstract class TrajectoryTracker {

    private var trajectoryIterator: TrajectoryIterator<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>? = null
    private var deltaTimeController = DeltaTime()
    private var previousVelocity: TrajectoryTrackerVelocityOutput? = null

    val referencePoint get() = trajectoryIterator?.currentState
    val isFinished get() = trajectoryIterator?.isDone ?: true

    fun reset(trajectory: Trajectory<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>) {
        trajectoryIterator = trajectory.iterator()
        deltaTimeController.reset()
        previousVelocity = null
    }

    fun nextState(
            currentRobotPose: Pose2d,
            currentTime: SIUnit<Second> = System.currentTimeMillis().milli.second
    ): TrajectoryTrackerOutput {
        val iterator = trajectoryIterator
        require(iterator != null) {
            "You cannot get the next state from the TrajectoryTracker without a trajectory! Call TrajectoryTracker#reset first!"
        }
        val deltaTime = deltaTimeController.updateTime(currentTime)
        iterator.advance(deltaTime)

        val velocity = calculateState(iterator, currentRobotPose)
        val previousVelocity = this.previousVelocity
        this.previousVelocity = velocity

        // Calculate Acceleration (useful for drive dynamics)
        return if (previousVelocity == null || deltaTime.value <= 0) {
            TrajectoryTrackerOutput(
                    linearVelocity = velocity.linearVelocity,
                    linearAcceleration = 0.meter.acceleration,
                    angularVelocity = velocity.angularVelocity,
                    angularAcceleration = 0.radian.acceleration
            )
        } else {
            TrajectoryTrackerOutput(
                    linearVelocity = velocity.linearVelocity,
                    linearAcceleration = (velocity.linearVelocity - previousVelocity.linearVelocity) / deltaTime,
                    angularVelocity = velocity.angularVelocity,
                    angularAcceleration = (velocity.angularVelocity - previousVelocity.angularVelocity) / deltaTime
            )
        }
    }

    protected abstract fun calculateState(
            iterator: TrajectoryIterator<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>,
            robotPose: Pose2d
    ): TrajectoryTrackerVelocityOutput

    protected data class TrajectoryTrackerVelocityOutput(
            internal val linearVelocity: SIUnit<LinearVelocity>,
            internal val angularVelocity: SIUnit<AngularVelocity>
    )
}

data class TrajectoryTrackerOutput(
        val linearVelocity: SIUnit<LinearVelocity>,
        val linearAcceleration: SIUnit<LinearAcceleration>,
        val angularVelocity: SIUnit<AngularVelocity>,
        val angularAcceleration: SIUnit<AngularAcceleration>
)

