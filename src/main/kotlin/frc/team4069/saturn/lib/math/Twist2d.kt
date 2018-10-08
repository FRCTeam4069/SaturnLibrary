package frc.team4069.saturn.lib.math

import frc.team4069.saturn.lib.math.uom.distance.DistanceUnit
import frc.team4069.saturn.lib.util.MotorOutputs

/**
 * Class containing linear and angular velocities for a time. Returned by Ramsete controller
 */
data class Twist2d(val v: Double, val w: Double) {
    fun inverseKinematics(driveBaseWidth: DistanceUnit): MotorOutputs {
        val left = ((-driveBaseWidth.ft * w + 2 * v) / 2)
        val right = ((driveBaseWidth.ft * w + 2 * v) / 2)

        return MotorOutputs(left, right)
    }
}