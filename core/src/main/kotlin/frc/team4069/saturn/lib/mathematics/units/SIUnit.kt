package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.lerp
import kotlin.math.absoluteValue

/**
 * A newtype container representing some type-safe unit of measurement
 * The concrete unit this represents is encoded in <T>. Operations on this unit will yield new units whose generic is modified
 * according to the operation performed
 *
 * Conversions from this wrapper back to doubles can be found for each defined unit under the conversions package. If something is not defined,
 * scaling the value returned from `value` appropriate to the prefix of the unit is equivalent.
 */
class SIUnit<T: Key>(val value: Double) : Comparable<SIUnit<T>> {
    operator fun plus(other: SIUnit<T>) = SIUnit<T>(value + other.value)
    operator fun minus(other: SIUnit<T>) = SIUnit<T>(value - other.value)
    operator fun times(scalar: Double) = SIUnit<T>(value * scalar)
    operator fun div(scalar: Double) = SIUnit<T>(value / scalar)

    operator fun <U: Key> div(other: SIUnit<U>) = SIUnit<Fraction<T, U>>(value / other.value)
    operator fun div(other: SIUnit<T>) = value / other.value
    operator fun unaryMinus() = this * -1.0

    fun invert(): SIUnit<Inverse<T>> = SIUnit(1.0 / value)

    fun lerp(endValue: SIUnit<T>, t: Double) = SIUnit<T>(value.lerp(endValue.value, t))

    val absoluteValue: SIUnit<T> get() = SIUnit(value.absoluteValue)

    infix fun epsilonEquals(other: SIUnit<T>) = value epsilonEquals other.value

    override fun compareTo(other: SIUnit<T>) = value.compareTo(other.value)
}

@JvmName("fracCancelTimes")
operator fun <T: Key, U: Key> SIUnit<Fraction<T, U>>.times(other: SIUnit<U>) = SIUnit<T>(value * other.value)
@JvmName("fracCancelTimesCommute")
operator fun <T: Key, U: Key> SIUnit<U>.times(other: SIUnit<Fraction<T, U>>) = SIUnit<T>(value * other.value)

operator fun <T: Key, U: Key> SIUnit<T>.times(other: SIUnit<U>) = SIUnit<Mult<T, U>>(value * other.value)
