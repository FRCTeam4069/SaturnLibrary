package frc.team4069.saturn.lib.motor

import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Volt
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel

/**
 * Represents a platform-agnostic smart motor controller
 */
interface SaturnMotor<T: SIUnit<T>> {
    val model: NativeUnitModel<T>

    val sensorPosition: T
    val sensorVelocity: Velocity<T>
    val motorOutputVoltage: Volt

    /**
     * Sets a percent output to the motor controller
     */
    fun setPercentOutput(duty: Double)

    /**
     * Sets a position closed loop to the motor controller
     */
    fun setPosition(unit: T)

    /**
     * Sets a velocity closed loop to the motor controller, with optional arbitrary feedforward
     */
    fun setClosedLoopVelocity(velocity: Velocity<T>, arbitraryFeedForward: Double = 0.0)
}