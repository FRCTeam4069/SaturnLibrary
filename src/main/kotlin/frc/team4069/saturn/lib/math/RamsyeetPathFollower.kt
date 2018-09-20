package frc.team4069.saturn.lib.math

import frc.team4069.saturn.lib.math.geometry.Pose2d
import frc.team4069.saturn.lib.math.geometry.Twist2d
import jaci.pathfinder.Trajectory
import kotlin.math.PI
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
                           private val b: Double) {
    // The current segment of the path that we are at
    private var segmentIdx = 0

    init {
        if (zeta !in 0.0..1.0) {
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
    fun update(pose: Pose2d): Twist2d {
        if (segmentIdx < trajectory.length()) {
            val segment = trajectory[segmentIdx]

            // Get the desired angular velocity for this part of the segmentIdx. at the final time slice w_d = 0
            val wDesired = if (this.segmentIdx == trajectory.length() - 1) {
                0.0
            } else {
                (trajectory.segments[this.segmentIdx + 1].heading - segment.heading) / segment.dt
            }

            // Determine error in the x and y given the current posture of the robot and where it should be at this point
            val xError = segment.x - pose.x
            val yError = segment.y - pose.y

            // Determine error in the robot's heading given the current postures
            // At least 1E-9 to prevent division by 0 errors
            val thetaError = (segment.heading - pose.theta).bounded()
                    .let { if (it epsilonEquals 0.0) epsilon else it } // Ensure thetaError > 0 as to not cause division by 0 errors in the algorithm

            val error = pose.errorBy(segment.toPose())

            // v = v_d * cos(theta_d - theta) + k1(v_d, w_d)(cos(theta)(x_d-x) + sin(theta)(y_d-y))
            var velocity = (segment.velocity * error.rotation.cos) + (k1(segment.velocity, wDesired)) * (error.x)
//            var velocity = (segmentIdx.velocity * cos(thetaError)) + (k1(segmentIdx.velocity, wDesired)
//                    * ((cos(pose.theta) * xError) + (sin(pose.theta) * yError)))
            velocity = velocity.coerceIn(-safeV, safeV)

            // w = w_d + b*v_d*(sin(theta_d-theta)/(theta_d-theta))(cos(theta)(y_d-y) - sin(theta)(x_d-x)) + k1(v_d, w_d)(theta_d - theta)
            var angularVelocity = wDesired + (b *
                    segment.velocity * ((sin(error.theta)) / error.theta)) *
                    error.y + (k1(segment.velocity, wDesired) * error.theta)
//            var angularVelocity = wDesired + (b *
//                    segmentIdx.velocity * ((sin(thetaError)) / thetaError)) *
//                    ((cos(pose.theta) * yError) - (sin(pose.theta) * xError)) +
//                    (k1(segmentIdx.velocity, wDesired) * thetaError)
            angularVelocity = angularVelocity.coerceIn(-safeW, safeW)
            this.segmentIdx++

            return Twist2d(velocity, 0.0, angularVelocity)

        } else return Twist2d(0.0, 0.0, 0.0)
    }

    fun getCurrentSegment() = trajectory.segments.getOrNull(this.segmentIdx)

    /**
     * Gain function as described in eq 5.12
     */
    private fun k1(v: Double, w: Double): Double {
        return 2 * zeta * sqrt((w * w) + (b * v * v))
    }

    // boundHalfRadians from Pathfinder
    private fun Double.bounded(): Double {
        var x = this
        while (x >= PI) x -= (2 * PI)
        while (x < -PI) x += (2 * PI)

        return x
    }

    val isFinished: Boolean
        get() = this.segmentIdx >= trajectory.length()

    // Converts a PF Segment into a Pose2d to make it work with my API
    private fun Trajectory.Segment.toPose(): Pose2d {
        return Pose2d(x, y, heading)
    }

    companion object {
        const val safeV = 12.0
        const val safeW = PI
    }
}
