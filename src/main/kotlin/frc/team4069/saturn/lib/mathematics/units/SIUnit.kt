package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity

interface SIUnit<T : SIUnit<T>> : SIValue<T> {
    /**
     * This is the value expressed in its SI Base Unit
     */
    override val value: Double

    @Suppress("UNCHECKED_CAST")
    operator fun div(other: Time) = Velocity(value / other.value, this as T)
}