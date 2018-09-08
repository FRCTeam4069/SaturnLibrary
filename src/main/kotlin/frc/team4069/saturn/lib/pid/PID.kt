package frc.team4069.saturn.lib.pid

import frc.team4069.saturn.lib.util.systemTimeMillis

/**
 * Standard synchronous PID controller (must be manually controlled)
 */
class PID(constants: PIDConstants, target: Double, val deadband: Double = 0.01, val decayIntegral: Boolean = false) {

    var constants: PIDConstants = constants
        set(newConstants) {
            field = newConstants

            reset()
        }


    var target: Double = target
        set(newTarget) {
            field = newTarget

            reset()
        }

    private var integral = 0.0
    private var derivative = 0.0

    private var lastTime = 0.0
    private var lastError = 0.0

    fun update(value: Double): Double {
        // Calculate error
        val error = target - value

        // Calculate delta time
        val time = systemTimeMillis / 1000.0
        val dt = time - lastTime
        lastTime = time

        // If we've had at least one iteration, calculate derivative
        derivative = (error - lastError) * dt

        // Calculate integral
        integral += error * dt
        if (decayIntegral) {
            integral *= 0.75
        }

        // Calculate output
        val output = constants.kP * error
                   + constants.kI * integral
                   + constants.kD * derivative
                   + constants.kF * target

        lastError = error

        return output
    }

    fun clearIntegral() {
        integral = 0.0
    }

    fun reset() {
        integral = 0.0
        lastTime = Double.NaN
        lastError = Double.NaN
    }

    /**
     * Returns whether the PID controller is currently at the target (error value is near zero)
     */
    val atTargetP: Boolean
        get() = Math.abs(lastError) < deadband

    /**
     * Returns whether the PID controller is currently at a steady-state (derivative is near zero)
     */
    val atTargetD: Boolean
        get() = Math.abs(derivative) < (deadband / 10.0)

    /**
     * Returns whether the PID controller is currently at a steady-state at the target (both error and derivative are near zero)
     */
    val atTarget: Boolean
        get() = atTargetP && atTargetD

}