package frc.team4069.saturn.lib.motor

import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.derived.Acceleration
import frc.team4069.saturn.lib.mathematics.units.derived.Velocity

typealias LinearFalconMotor = SaturnMotor<Meter>
typealias AngularFalconMotor = SaturnMotor<Radian>

interface SaturnMotor<T : Key> {

    /**
     * The encoder attached to the motor
     */
    val encoder: SaturnEncoder<T>
    /**
     * The voltage output of the motor controller in volts
     */
    val voltageOutput: Double

    /**
     * Inverts the output given to the motor
     */
    var outputInverted: Boolean

    /**
     *  When enabled, motor leads are commonized electrically to reduce motion
     */
    var brakeMode: Boolean

    /**
     * Configures the max voltage output given to the motor
     */
    var voltageCompSaturation: Double

    /**
     *  Peak target velocity that the on board motion profile generator will use
     *  Unit is [T]/s
     */
    var motionProfileCruiseVelocity: SIUnit<Velocity<T>>
    /**
     *  Acceleration that the on board motion profile generator will
     *  Unit is [T]/s/s
     */
    var motionProfileAcceleration: SIUnit<Acceleration<T>>
    /**
     * Enables the use of on board motion profiling for position mode
     */
    var useMotionProfileForPosition: Boolean

    fun follow(motor: SaturnMotor<*>): Boolean

    /**
     * Sets the output [voltage] in volts and [arbitraryFeedForward] in volts
     */
    fun setVoltage(voltage: Double, arbitraryFeedForward: Double = 0.0)

    /**
     * Sets the output [dutyCycle] in percent and [arbitraryFeedForward] in volts
     */
    fun setDutyCycle(dutyCycle: Double, arbitraryFeedForward: Double = 0.0)

    /**
     * Sets the output [velocity] in [T]/s and [arbitraryFeedForward] in volts
     */
    fun setVelocity(velocity: SIUnit<Velocity<T>>, arbitraryFeedForward: Double = 0.0)

    /**
     * Sets the output [position] in [T] and [arbitraryFeedForward] in volts
     */
    fun setPosition(position: SIUnit<T>, arbitraryFeedForward: Double = 0.0)

    /**
     * Sets the output of the motor to neutral
     */
    fun setNeutral()
}