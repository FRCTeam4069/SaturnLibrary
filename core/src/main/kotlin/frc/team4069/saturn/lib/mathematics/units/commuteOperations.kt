package frc.team4069.saturn.lib.mathematics.units

operator fun <T: Key, U: Key> SIUnit<Mult<T, U>>.plus(other: SIUnit<Mult<U, T>>) = SIUnit<Mult<T, U>>(value + other.value)
operator fun <T: Key, U: Key> SIUnit<Mult<T, U>>.minus(other: SIUnit<Mult<U, T>>) = SIUnit<Mult<T, U>>(value - other.value)
operator fun <T: Key, U: Key> SIUnit<Mult<T, U>>.div(other: SIUnit<Mult<U, T>>) = value / other.value