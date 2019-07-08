package frc.team4069.saturn.lib.mathematics.twodim.control

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rectangle2d
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedEntry
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedTrajectory
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TrajectorySamplePoint
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.util.DeltaTime

abstract class TrajectoryTracker {
    private var currentTrajectory: TimedTrajectory<Pose2dWithCurvature>? = null
    protected val locationMarkers = mutableMapOf<Rectangle2d, () -> Unit>()
    private var previousVelocity: TrajectoryTrackerVelocityOutput? = null
    private val deltaTime = DeltaTime()

    val iterator get() = currentTrajectory?.iterator()
    val referencePoint get() = iterator?.currentState

    val isFinished
        get() = iterator?.isDone ?: true

    fun addMarker(area: Rectangle2d, callback: () -> Unit) {
        locationMarkers += area to callback
    }

    fun reset(trajectory: TimedTrajectory<Pose2dWithCurvature>) {
        currentTrajectory = trajectory
        locationMarkers.clear()
        previousVelocity = null
        deltaTime.reset()
    }

    fun update(robotState: Pose2d, currentTime: Time = System.currentTimeMillis().milli.second): TrajectoryTrackerOutput {
        if(this.iterator == null || this.isFinished) {
            return TrajectoryTrackerOutput()
        }
        val dt = deltaTime.updateTime(currentTime)
        iterator!!.advance(dt)

        val velocity = calculate(robotState, referencePoint!!) // referencePoint will never be null since we validated that the iterator is nonnull
        val previousVelocity = this.previousVelocity
        this.previousVelocity = velocity

        return if(previousVelocity == null) {
            TrajectoryTrackerOutput(
                    velocity.linearVelocity,
                    0.meter.acceleration,
                    velocity.angularVelocity,
                    0.degree.acceleration
            )
        }else {
            TrajectoryTrackerOutput(
                    velocity.linearVelocity,
                    (velocity.linearVelocity - previousVelocity.linearVelocity) / dt,
                    velocity.angularVelocity,
                    (velocity.angularVelocity - previousVelocity.angularVelocity) / dt
            )
        }
    }

    protected abstract fun calculate(robotState: Pose2d, referencePoint: TrajectorySamplePoint<TimedEntry<Pose2dWithCurvature>>): TrajectoryTrackerVelocityOutput

    protected data class TrajectoryTrackerVelocityOutput(
            val linearVelocity: LinearVelocity,
            val angularVelocity: AngularVelocity
    )

    private fun TrajectoryTrackerOutput.toVelocityOutput() = TrajectoryTrackerVelocityOutput(linearVelocity, angularVelocity)

    data class TrajectoryTrackerOutput(
            val linearVelocity: LinearVelocity,
            val linearAcceleration: LinearAcceleration,
            val angularVelocity: AngularVelocity,
            val angularAcceleration: AngularAcceleration
    ) {
        constructor() : this(0.meter.velocity, 0.meter.acceleration, 0.degree.velocity, 0.degree.acceleration)
    }
}