package frc.team4069.saturn.lib.motor

import frc.team4069.saturn.lib.mathematics.units.Key
import frc.team4069.saturn.lib.mathematics.units.NativeUnit
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.derived.Velocity
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitVelocity

interface SaturnEncoder<T: Key> {
    /**
     * The velocity of the encoder in [T]/s
     */
    val velocity: SIUnit<Velocity<T>>
    /**
     * The position of the encoder in [T]
     */
    val position: SIUnit<T>

    /**
     * The velocity of the encoder in NativeUnits/s
     */
    val rawVelocity: SIUnit<NativeUnitVelocity>
    /**
     * The position of the encoder in NativeUnits
     */
    val rawPosition: SIUnit<NativeUnit>

    fun resetPosition(newPosition: SIUnit<T>)

}
