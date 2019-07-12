package frc.team4069.saturn.lib.mathematics.units

operator fun <T: Key, U: Key> SIUnit<T>.div(other: SIUnit<Mult<T, U>>) = SIUnit<Inverse<U>>(value / other.value)
