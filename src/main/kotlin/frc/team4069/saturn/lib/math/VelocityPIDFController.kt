package frc.team4069.saturn.lib.math

import kotlin.math.abs
import kotlin.math.sign

class VelocityPIDFController(private val p: Double = 0.0,
                             private val i: Double = 0.0,
                             private val d: Double = 0.0,
                             private val v: Double = 0.0,
                             private val a: Double = 0.0,
                             private val s: Double = 0.0,
                             private val iLimit: Double = 0.0,
                             private val deadband: Double = 0.1,
                             private val currentVelocity: () -> Double) {
    private var lastError = 0.0
    private var derivative = 0.0
    private var integral = 0.0

    private var lastCallTime = -1L
    private var dt = -1L

    fun update(target: Pair<Double, Double>): Double {
        val (targetVel, targetAcc) = target

        val current = currentVelocity()

        val time = System.currentTimeMillis()
        dt = if(lastCallTime < 0) {
            lastCallTime = time
            0L
        }else {
            time - lastCallTime
        }

        val error = targetVel - current

        integral += error * dt
        derivative += (error - lastError) / dt

        if(integral > iLimit && iLimit != 0.0)
            integral = iLimit

        if(abs(targetVel) < deadband) return 0.0

        val output = p * error + i * integral + d * derivative +
                v * targetVel + a * targetAcc + s * sign(targetVel)

        lastError = error
        lastCallTime = time

        return output
    }

}