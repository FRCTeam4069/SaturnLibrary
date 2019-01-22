package frc.team4069.saturn.lib.mathematics.twodim.control

import com.team254.lib.physics.DifferentialDrive
import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedTrajectory
import frc.team4069.saturn.lib.mathematics.units.derivedunits.feetPerSecond
import frc.team4069.saturn.lib.mathematics.units.millisecond
import frc.team4069.saturn.lib.util.DeltaTime
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

// https://www.dis.uniroma1.it/~labrob/pub/papers/Ramsete01.pdf
// Equation 5.12
open class RamseteController(
    val trajectory: TimedTrajectory<Pose2dWithCurvature>,
    private val b: Double,
    private val zeta: Double
) {

    init {
        if (zeta !in 0..1) {
            throw IllegalArgumentException("Zeta must be in (0, 1)")
        }

        if (b <= 0) {
            throw IllegalArgumentException("b must be > 0")
        }
    }

    private val locationListeners = mutableMapOf<Pose2d, () -> Unit>()

    var lastTime = -1L
    val iterator = trajectory.iterator()

    val referencePoint
        get() = iterator.currentState

    val referencePose
        get() = referencePoint.state.state.pose

    val isFinished
        get() = iterator.isDone

    val deltaTimeController = DeltaTime()

    fun addMarker(atPose: Pose2d, callback: () -> Unit) = locationListeners.put(atPose, callback)

    fun update(robotPose: Pose2d, currentTime: Long = System.currentTimeMillis()): DifferentialDrive.ChassisState {
        val dt = deltaTimeController.updateTime(currentTime.millisecond)

        val error = referencePose inFrameOfReferenceOf robotPose
        val vd = referencePoint.state.velocity.value // m/s
        val wd = vd * referencePoint.state.state.curvature.curvature.value // thank you prateek, very cool!

        val angleError = error.rotation.radian

        val v = vd * error.rotation.cos + k1(vd, wd) * error.translation.x.feet
        val w = wd + b * vd * sinc(angleError) * error.translation.y.feet + k1(vd, wd) * angleError

        iterator.advance(dt)

        lastTime = currentTime

        locationListeners.asSequence().filter { robotPose fuzzyEquals it.key }
            .forEach { (pose, listener) ->
                locationListeners.remove(pose)
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

