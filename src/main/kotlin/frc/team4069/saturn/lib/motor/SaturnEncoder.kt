package frc.team4069.saturn.lib.motor

import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnit
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitVelocity

interface SaturnEncoder<T : SIUnit<T>> {
    /**
     * The velocity of the encoder in [T]/s
     */
    val velocity: Velocity<T>
    /**
     * The position of the encoder in [T]
     */
    val position: T

    /**
     * The velocity of the encoder in NativeUnits/s
     */
    val rawVelocity: NativeUnitVelocity
    /**
     * The position of the encoder in NativeUnits
     */
    val rawPosition: NativeUnit

    fun resetPosition(newPosition: Double)

}
