package frc.team4069.saturn.lib.mathematics.twodim.control

import com.team254.lib.physics.DifferentialDrive
import edu.wpi.first.wpilibj.Timer
import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rectangle2d
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedEntry
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedTrajectory
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TrajectorySamplePoint
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.util.DeltaTime
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

// https://www.dis.uniroma1.it/~labrob/pub/papers/Ramsete01.pdf
// Equation 5.12
class RamseteTracker(private val b: Double, private val zeta: Double) : TrajectoryTracker() {
    init {
        if (zeta !in 0.0..1.0) {
            throw IllegalArgumentException("Zeta must be in (0, 1)")
        }

        if (b <= 0.0) {
            throw IllegalArgumentException("b must be > 0")
        }
    }

    override fun calculate(robotState: Pose2d, referencePoint: TrajectorySamplePoint<TimedEntry<Pose2dWithCurvature>>): TrajectoryTrackerVelocityOutput {
        val referencePose = referencePoint.state.state.pose

        val error = referencePose inFrameOfReferenceOf robotState
        val vd = referencePoint.state.velocity.value // m/s
        val wd = vd * referencePoint.state.state.curvature.curvature.value // thank you prateek, very cool!

        val angleError = error.rotation.value

        val v = vd * error.rotation.cos + k1(vd, wd) * error.translation.xRaw
        val w = wd + b * vd * sinc(angleError) * error.translation.yRaw + k1(vd, wd) * angleError

        locationMarkers.filter { (rect, _) -> rect.contains(robotState.translation) }
                .forEach { (rect, listener) ->
                    locationMarkers.remove(rect)
                    listener()
                }

        return TrajectoryTrackerVelocityOutput(v.meter.velocity, w.radian.velocity)
    }

    fun k1(vd: Double, wd: Double): Double {
        return 2 * zeta * sqrt(wd.pow(2) + b * vd.pow(2))
    }

    companion object {
        private fun sinc(theta: Double): Double {
            return if (theta epsilonEquals 0.0) {
                1.0 // return the limit in cases where it approaches 0
            } else {
                sin(theta) / theta
            }
        }
    }
}

// https://www.dis.uniroma1.it/~labrob/pub/papers/Ramsete01.pdf
// Equation 5.12
@Deprecated("Control abstraction is being replaced with TrajectoryTracker class", ReplaceWith("RamseteTracker"))
open class RamseteController(
        val trajectory: TimedTrajectory<Pose2dWithCurvature>,
        private val b: Double,
        private val zeta: Double
) {

    init {
        if (zeta !in 0.0..1.0) {
            throw IllegalArgumentException("Zeta must be in (0, 1)")
        }

        if (b <= 0) {
            throw IllegalArgumentException("b must be > 0")
        }
    }

    private val locationListeners = mutableMapOf<Rectangle2d, () -> Unit>()

    val iterator = trajectory.iterator()

    val referencePoint
        get() = iterator.currentState

    val referencePose
        get() = referencePoint.state.state.pose

    val isFinished
        get() = iterator.isDone

    val deltaTimeController = DeltaTime()

    fun addMarker(container: Rectangle2d, callback: () -> Unit) = locationListeners.put(container, callback)

    fun update(robotPose: Pose2d, currentTime: Time = Timer.getFPGATimestamp().second): DifferentialDrive.ChassisState {
        val dt = deltaTimeController.updateTime(currentTime)

        val error = referencePose inFrameOfReferenceOf robotPose
        val vd = referencePoint.state.velocity.value // m/s
        val wd = vd * referencePoint.state.state.curvature.curvature.value // thank you prateek, very cool!

        val angleError = error.rotation.radian

        val v = vd * error.rotation.cos + k1(vd, wd) * error.translation.xRaw
        val w = wd + b * vd * sinc(angleError) * error.translation.yRaw + k1(vd, wd) * angleError

        iterator.advance(dt)

        locationListeners.filter { (rect, _) -> rect.contains(robotPose.translation) }
                .forEach { (rect, listener) ->
                    locationListeners.remove(rect)
                    listener()
                }

        return DifferentialDrive.ChassisState(
                linear = v,
                angular = w
        )
    }

    fun k1(vd: Double, wd: Double): Double {
        return 2 * zeta * sqrt(wd.pow(2) + b * vd.pow(2))
    }

    companion object {
        private fun sinc(theta: Double): Double {
            return if (theta epsilonEquals 0.0) {
                1.0 // return the limit in cases where it approaches 0
            } else {
                sin(theta) / theta
            }
        }
    }
}

