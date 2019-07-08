package frc.team4069.saturn.lib.mathematics.units.derived

import frc.team4069.saturn.lib.mathematics.units.*

typealias VelocityT<T> = Fraction<T, Second>
typealias AccelerationT<T> = Fraction<VelocityT<T>, Second>

typealias Hertz = Inverse<Second>
typealias CurvatureT = Inverse<Meter>

typealias Coulomb = Mult<Ampere, Second>
typealias Volt = Fraction<Joule, Coulomb>
typealias Ohm = Fraction<Volt, Ampere>

typealias Newton = Mult<Kilogram, AccelerationT<Meter>>
typealias Joule = Mult<Newton, Meter>
typealias Watt = Fraction<Joule, Second>
