package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.lerp
import frc.team4069.saturn.lib.mathematics.max
import frc.team4069.saturn.lib.mathematics.min
import kotlin.math.absoluteValue

/**
 * A newtype container representing some type-safe unit of measurement
 * The concrete unit this represents is encoded in <T>. Operations on this unit will yield new units whose generic is modified
 * according to the operation performed
 *
 * Conversions from this wrapper back to doubles can be found for each defined unit under the conversions package. If something is not defined,
 * scaling the value returned from `value` appropriate to the prefix of the unit is equivalent.
 *
 * NOTE: Comparisons using == and != should NOT be used with this wrapper, as it disregards the unit type and only compares the value stored within.
 * <, <=, >, and >= however, are typechecked and safe to use
 */
inline class SIUnit<T: Key>(val value: Double) : Comparable<SIUnit<T>> {
    operator fun unaryMinus() = SIUnit<T>(-value)

    operator fun plus(other: SIUnit<T>) =
        SIUnit<T>(value + other.value)
    operator fun minus(other: SIUnit<T>) =
        SIUnit<T>(value - other.value)
    operator fun times(scalar: Double) = SIUnit<T>(value * scalar)
    operator fun div(scalar: Double) = SIUnit<T>(value / scalar)

    operator fun div(other: SIUnit<T>) = value / other.value

    override fun compareTo(other: SIUnit<T>) = value.compareTo(other.value)

    infix fun epsilonEquals(other: SIUnit<T>) = value epsilonEquals other.value
    fun lerp(endValue: SIUnit<T>, t: Double) = SIUnit<T>(value.lerp(endValue.value, t))

    fun pow2(): SIUnit<Mult<T, T>> = this * this
    fun pow3(): SIUnit<Mult<Mult<T, T>, T>> = this * this * this

    fun invert() = SIUnit<Inverse<T>>(1.0 / value)

    fun safeRangeTo(endInclusive: SIUnit<T>) = min(this, endInclusive)..max(this, endInclusive)

    val absoluteValue get() = SIUnit<T>(value.absoluteValue)
}

operator fun <T: Key, U: Key> SIUnit<T>.div(other: SIUnit<U>) =
    SIUnit<Fraction<T, U>>(value / other.value)
operator fun <T: Key, U: Key> SIUnit<T>.times(other: SIUnit<U>) =
    SIUnit<Mult<T, U>>(value * other.value)

operator fun <T: Key> Number.times(other: SIUnit<T>) = other * toDouble()
