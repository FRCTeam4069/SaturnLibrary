package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.units.derived.*


/**
 * A generic class to construct any unit with the given prefix scalar
 *
 * An instance of this class will be constructed by the unitless prefixes defined below, at which point a unit can be chosen with the given prefix.
 */
class UnitBuilder internal constructor(val value: Double, val prefixScalar: Double) {
    val meter get() = SIUnit<Meter>(value * prefixScalar)
    val gram get() = SIUnit<Kilogram>(value * prefixScalar / 1000.0)
    val second get() = SIUnit<Second>(value * prefixScalar)
    val amp get() = SIUnit<Ampere>(value * prefixScalar)
    val coulomb get() = SIUnit<Coulomb>(value * prefixScalar)
    val volt get() = SIUnit<Volt>(value * prefixScalar)
    val ohm get() = SIUnit<Ohm>(value * prefixScalar)
    val newton get() = SIUnit<Newton>(value * prefixScalar)
    val joule get() = SIUnit<Joule>(value * prefixScalar)
    val hertz get() = SIUnit<Hertz>(value * prefixScalar)
    val curvature get() = SIUnit<CurvatureT>(value * prefixScalar)
    val radian get() = SIUnit<Radian>(value * prefixScalar)
    val degree get() = SIUnit<Radian>(Math.toRadians(value) * prefixScalar)
}

val Number.yotta get() = UnitBuilder(toDouble(), SIConstants.kYotta)
val Number.zetta get() = UnitBuilder(toDouble(), SIConstants.kZetta)
val Number.exa get() = UnitBuilder(toDouble(), SIConstants.kExa)
val Number.peta get() = UnitBuilder(toDouble(), SIConstants.kPeta)
val Number.tera get() = UnitBuilder(toDouble(), SIConstants.kTera)
val Number.giga get() = UnitBuilder(toDouble(), SIConstants.kGiga)
val Number.mega get() = UnitBuilder(toDouble(), SIConstants.kMega)
val Number.kilo get() = UnitBuilder(toDouble(), SIConstants.kKilo)
val Number.hecto get() = UnitBuilder(toDouble(), SIConstants.kHecto)
val Number.deca get() = UnitBuilder(toDouble(), SIConstants.kDeca)
// unit is omitted because scalar is 1
val Number.deci get() = UnitBuilder(toDouble(), SIConstants.kDeci)
val Number.centi get() = UnitBuilder(toDouble(), SIConstants.kCenti)
val Number.milli get() = UnitBuilder(toDouble(), SIConstants.kMilli)
val Number.micro get() = UnitBuilder(toDouble(), SIConstants.kMicro)
val Number.nano get() = UnitBuilder(toDouble(), SIConstants.kNano)
val Number.pico get() = UnitBuilder(toDouble(), SIConstants.kPico)
val Number.femto get() = UnitBuilder(toDouble(), SIConstants.kFemto)
val Number.atto get() = UnitBuilder(toDouble(), SIConstants.kAtto)
val Number.zepto get() = UnitBuilder(toDouble(), SIConstants.kZepto)
val Number.yocto get() = UnitBuilder(toDouble(), SIConstants.kYocto)


val Number.meter get() = SIUnit<Meter>(toDouble())
val <T : Key> SIUnit<T>.velocity get() = SIUnit<VelocityT<T>>(value)
val <T : Key> SIUnit<T>.acceleration get() = SIUnit<AccelerationT<T>>(value)

/**
 * Prefixless getters for all defined units.
 */
val Number.gram get() = SIUnit<Kilogram>(toDouble() / 1000.0)
val Number.second get() = SIUnit<Second>(toDouble())
val Number.amp get() = SIUnit<Ampere>(toDouble())
val Number.coulomb get() = SIUnit<Coulomb>(toDouble())
val Number.volt get() = SIUnit<Volt>(toDouble())
val Number.ohm get() = SIUnit<Ohm>(toDouble())
val Number.newton get() = SIUnit<Newton>(toDouble())
val Number.joule get() = SIUnit<Joule>(toDouble())
val Number.hertz get() = SIUnit<Hertz>(toDouble())
val Number.curvature get() = SIUnit<CurvatureT>(toDouble())

val Number.radian get() = SIUnit<Radian>(toDouble())
val Number.degree get() = SIUnit<Radian>(Math.toRadians(toDouble()))

