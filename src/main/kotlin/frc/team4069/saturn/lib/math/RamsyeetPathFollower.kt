package frc.team4069.saturn.lib.math

import frc.team4069.saturn.lib.util.MotorOutputs
import jaci.pathfinder.Trajectory
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Implementation of Ramsete01 Equation 5.12 for path following
 * 2 control gains are used to follow a path
 * zeta dampens (Can be thought of as analogous to kD in PID control)
 * b increases correction (Can be thought of as analogous to kP in PID control)
 */
class RamsyeetPathFollower(private val trajectory: Trajectory,
                           private val zeta: Double,
                           private val b: Double,
                           private val driveBaseWidth: Double) {
    var segment = 0

    init {
        if (zeta !in 0..1) {
            throw IllegalArgumentException("zeta must be in (0, 1)")
        }

        if (b <= 0) {
            throw IllegalArgumentException("b must be greater than 0")
        }
    }

    /**
     * Returns motor outputs for following [trajectory]. Calculations based off of the current pose of the robot encapsulated in [pose]
     * Must be called at the dt of [trajectory]
     */
    fun update(pose: Pose2d): MotorOutputs {
        if (segment < trajectory.length()) {
            val segment = trajectory.segments[segment]
            val wDesired = if (this.segment == trajectory.length() - 1) {
                0.0
            } else {
                (trajectory.segments[this.segment + 1].heading - segment.heading) / segment.dt
            }

            val xError = segment.x - pose.x
            val yError = segment.y - pose.y
            val thetaError = (segment.heading - pose.theta).bounded()
                    .let { if (it epsilonEquals 0.0) epsilon else it } // Ensure thetaError > 0 as to not cause division by 0 errors in the algorithm


            val velocity = (segment.velocity * cos(thetaError)) + (k1(segment.velocity, wDesired)
                    * ((cos(pose.theta) * xError) + (sin(pose.theta) * yError)))

            val angularVelocity = wDesired + (b *
                    segment.velocity * ((sin(thetaError)) / thetaError)) *
                    ((cos(pose.theta) * yError) - (sin(pose.theta) * xError)) +
                    (k1(segment.velocity, wDesired) * thetaError)
            this.segment++

            // Convert calculated linear and angular velocities into PercentOutput values for motor controllers
            val left = (-driveBaseWidth * angularVelocity + 2 * velocity) / 2
            val right = (driveBaseWidth * angularVelocity + 2 * velocity) / 2

            return MotorOutputs(left, right)

        } else return MotorOutputs(0.0, 0.0)
    }

    /**
     * Gain function as described in eq 5.12
     */
    private fun k1(v: Double, w: Double): Double {
        return 2 * zeta * sqrt((w * w) + (b * v * v))
    }

    private fun Double.bounded(): Double {
        var x = this
        while(x >= PI) x -= (2 * PI)
        while(x < PI) x += (2 * PI)

        return x
    }

    val isFinished: Boolean
        get() = this.segment >= trajectory.length()
}