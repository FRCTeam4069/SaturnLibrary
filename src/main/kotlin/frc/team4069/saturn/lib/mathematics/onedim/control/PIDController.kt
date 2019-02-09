package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.units.millisecond
import frc.team4069.saturn.lib.util.DeltaTime
import kotlin.math.abs

/**
 * Basic implementation of a PID controller without feedforward
 */
class PIDController {
    private val deltaController = DeltaTime()
    private var lastInput = Double.NaN
    private var lastError = Double.NaN
    private var integral = 0.0

    // Proportional gain
    var kP = 0.0
    // Integral gain
    var kI = 0.0
    // Derivative gain
    var kD = 0.0
    // Threshold at which the integral will be considered 0.0
    var integralDeadband = 0.0
    // Tolerance for slight error between setpoint and actual input
    var tolerance = 0.0

    var setpoint = 0.0

    fun calculate(input: Double): Double {
        val dt = deltaController.updateTime(System.currentTimeMillis().millisecond).second
        val error = setpoint - input
        var output = kP * error

        if(lastError.isFinite()) {
            output += kD * ((error - lastError) / dt)
            integral += error * dt
            if(abs(integral) < integralDeadband) {
                integral = 0.0
            }

            output += kI * integral
        }

        lastError = error
        lastInput = input

        return output.coerceIn(-1.0..1.0)
    }

    val isFinished: Boolean
        get() = lastInput.isFinite() && abs(lastInput - setpoint) < tolerance
}