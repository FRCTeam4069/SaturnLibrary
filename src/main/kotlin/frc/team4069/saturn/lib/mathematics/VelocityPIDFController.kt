package frc.team4069.saturn.lib.mathematics

import frc.team4069.saturn.lib.math.epsilonEquals
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * implementation of a PID controller designed to close the loop on velocity
 *
 * Feedback gains are [p], [i], and [d]. Feedforward gains are
 * Velocity feedforward [v]
 * Acceleration feedforward [a]
 * Static feedforward [s]
 *
 * [currentVelocity] is used to get the actual velocity of the robot for calculations
 */
class VelocityPIDFController(private val p: Double = 0.0,
                             private val i: Double = 0.0,
                             private val d: Double = 0.0,
                             private val v: Double = 0.0,
                             private val a: Double = 0.0,
                             private val s: Double = 0.0,
                             private val kILimit: Double = 0.0,
                             private val kDeadband: Double = 0.1,
                             private val currentVelocity: () -> Double) {


    // Stores PID related variables
    private var lastError = 0.0
    private var derivative = 0.0
    private var integral = 0.0


    // Looping related variables
    private var lastCallTime = -1.0
    private var dt = -1.0

    /**
     * Updates the controller with the new current values against the setpoint
     *
     * [targetVelocity] is required, used in feedback and kV and kS feedforward calculations
     * [targetAcceleration] is optional. Only required if kA is nonzero, can be 0.0 otherwise
     *
     * Returns a value between -1..1 to set to converge to the setpoint
     */
    fun getPIDFOutput(targetVelocity: Double, targetAcceleration: Double): Double {

        // Retrieve currentVelocity position
        val current = currentVelocity()

        // Get currentVelocity time
        val timeSeconds = System.nanoTime() / 1.0e+9
        dt = if (lastCallTime < 0) {
            lastCallTime = timeSeconds
            return 0.0
        } else timeSeconds - lastCallTime


        // Calculate error
        val error = targetVelocity - current

        // Calculate integral and derivative terms
        integral += error * dt
        derivative += (error - lastError) / dt

        // Enforce I Limit
        if (integral > kILimit && (kILimit epsilonEquals 0.0).not()) integral = kILimit

        // Enforce Deadband
        if (targetVelocity.absoluteValue < kDeadband) return 0.0

        // Calculate feedback and feedforward terms
        val feedback = (p * error) + (i * integral) + (d * derivative)
        val feedfrwd = (v * targetVelocity) + (a * targetAcceleration) + (s * sign(targetVelocity))

        // Store last loop information
        lastError = error
        lastCallTime = timeSeconds

        // Return output
        return feedback + feedfrwd
    }
}
