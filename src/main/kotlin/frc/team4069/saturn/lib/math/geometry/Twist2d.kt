package frc.team4069.saturn.lib.math.geometry

import frc.team4069.saturn.lib.math.uom.distance.DistanceUnit
import frc.team4069.saturn.lib.util.MotorOutputs
import kotlin.math.hypot

data class Twist2d(val dx: Double, val dy: Double, val dtheta: Double) {

    operator fun times(scalar: Double) = Twist2d(dx * scalar, dy * scalar, dtheta * scalar)

    fun norm(): Double {
        return if (dy == 0.0) dx else hypot(dx, dy)
    }

    fun inverseKinematics(wheelBase: DistanceUnit): MotorOutputs {
        val left = ((-wheelBase.ft * dtheta + 2 * dx) / 2)
        val right = ((wheelBase.ft * dtheta + 2 * dx) / 2)


        return MotorOutputs(left, right)
    }
}