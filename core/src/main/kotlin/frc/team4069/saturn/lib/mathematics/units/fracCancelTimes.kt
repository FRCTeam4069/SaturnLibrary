//@file:JvmName("FracCancelTimes")
package frc.team4069.saturn.lib.mathematics.units

operator fun <T: Key, U: Key> SIUnit<Fraction<T, U>>.times(other: SIUnit<U>) =
    SIUnit<T>(value * other.value)