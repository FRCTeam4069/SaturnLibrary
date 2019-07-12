package frc.team4069.saturn.lib.mathematics.twodim.control

import com.team254.lib.physics.DifferentialDrive
import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rectangle2d
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.TrajectoryIterator
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedEntry
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedTrajectory
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TrajectorySamplePoint
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.util.DeltaTime
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class RamseteTracker(
        private val kBeta: Double,
        private val kZeta: Double
) : TrajectoryTracker() {

    /**
     * Calculate desired chassis velocity using Ramsete.
     */
    override fun calculateState(
            iterator: TrajectoryIterator<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>,
            robotPose: Pose2d
    ): TrajectoryTrackerVelocityOutput {
        val referenceState = iterator.currentState.state

        // Calculate goal in robot's coordinates
        val error = referenceState.state.pose inFrameOfReferenceOf robotPose

        // Get reference linear and angular velocities
        val vd = referenceState.velocity
        val wd = (vd * referenceState.state.curvature.curvature).value

        // Compute gain
        val k1 = 2 * kZeta * sqrt(wd * wd + kBeta * vd.value * vd.value)

        // Get angular error in bounded radians
        val angleError = error.rotation.radian

        return TrajectoryTrackerVelocityOutput(
                linearVelocity = vd * error.rotation.cos + (k1 * error.translation.x).velocity,
                angularVelocity = (wd + kBeta * vd.value * sinc(angleError) * error.translation.y.value + k1 * angleError).radian.velocity
        )
    }

    private operator fun <T: Key> Double.times(unit: SIUnit<T>) = unit * this

    companion object {
        private fun sinc(theta: Double) =
                if (theta epsilonEquals 0.0) {
                    1.0
                } else sin(theta) / theta
    }

}
