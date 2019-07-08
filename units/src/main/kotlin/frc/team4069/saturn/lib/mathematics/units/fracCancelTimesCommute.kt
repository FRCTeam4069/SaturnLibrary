//@file:JvmName("FracCancelTimesCommute")
package frc.team4069.saturn.lib.mathematics.units

operator fun <T: Key, U: Key> SIUnit<U>.times(other: SIUnit<Fraction<T, U>>) =
    SIUnit<T>(value * other.value)