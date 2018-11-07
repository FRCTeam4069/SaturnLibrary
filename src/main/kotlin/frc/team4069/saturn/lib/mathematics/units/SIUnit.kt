package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.units.expressions.SIExp2
import frc.team4069.saturn.lib.mathematics.units.fractions.SIFrac11


interface SIUnit<T : SIUnit<T>> : SIValue<T> {
    /**
     * This is the value expressed in its SI Base Unit
     */
    override val value: Double

    @Suppress("UNCHECKED_CAST")
    operator fun <B : SIUnit<B>> times(other: B) = SIExp2(value * other.value, this as T, other)

    @Suppress("UNCHECKED_CAST")
    operator fun <B : SIUnit<B>> div(other: B) = SIFrac11(value / other.value, this as T, other)
}