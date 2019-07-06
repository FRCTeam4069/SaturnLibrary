package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.kEpsilon
import frc.team4069.saturn.lib.mathematics.lerp
import frc.team4069.saturn.lib.mathematics.max
import frc.team4069.saturn.lib.mathematics.min
import kotlin.math.absoluteValue

operator fun <T : SIValue<T>> Number.times(other: T) = other * this

@Deprecated("All legacy units are to be replaced with YAUL in the near future.")
interface SIValue<T : SIValue<T>> : Comparable<T> {
    val value: Double

    fun createNew(newValue: Double): T

    val absoluteValue get() = createNew(value.absoluteValue)

    operator fun unaryMinus() = createNew(-value)

    // Operators with SI Units

    operator fun plus(other: T) = createNew(value + other.value)
    operator fun minus(other: T) = createNew(value - other.value)
    // operator fun times(other: T) = TODO()

    operator fun div(other: T) = value / other.value
    operator fun rem(other: T) = value % other.value

    // Operators with Scalars

    operator fun times(other: Number) = createNew(value * other.toDouble())
    operator fun div(other: Number) = createNew(value / other.toDouble())
    operator fun rem(other: Number) = createNew(value % other.toDouble())

    override operator fun compareTo(other: T) = value.compareTo(other.value)

    // Misc

    fun lerp(endValue: T, t: Double) = createNew(value.lerp(endValue.value, t))
    infix fun epsilonEquals(other: T) = (this.value - other.value).absoluteValue < kEpsilon

    @Suppress("UNCHECKED_CAST")
    fun safeRangeTo(endInclusive: T) = min(this as T, endInclusive)..max(this, endInclusive)
}