package frc.team4069.saturn.lib.mathematics.units

operator fun <T: Key, U: Key> SIUnit<T>.times(other: SIUnit<Fraction<Fraction<Unitless, U>, U>>): SIUnit<Fraction<Fraction<T, U>, U>> = SIUnit(this.value * other.value)