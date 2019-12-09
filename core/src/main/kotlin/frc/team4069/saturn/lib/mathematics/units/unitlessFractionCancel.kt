package frc.team4069.saturn.lib.mathematics.units

operator fun <T: Key, U: Key> SIUnit<T>.times(other: SIUnit<Fraction<Unitless, U>>): SIUnit<Fraction<T, U>> = SIUnit(this.value * other.value)