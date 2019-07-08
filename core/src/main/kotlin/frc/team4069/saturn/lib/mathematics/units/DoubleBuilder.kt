package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.units.derived.*

/**
 * Prefixless getters for SI constants.
 */
val Double.yotta get() = UnitBuilder(toDouble() * SIConstants.kYotta)
val Double.zetta get() = UnitBuilder(toDouble() * SIConstants.kZetta)
val Double.exa get() = UnitBuilder(toDouble() * SIConstants.kExa)
val Double.peta get() = UnitBuilder(toDouble() * SIConstants.kPeta)
val Double.tera get() = UnitBuilder(toDouble() * SIConstants.kTera)
val Double.giga get() = UnitBuilder(toDouble() * SIConstants.kGiga)
val Double.mega get() = UnitBuilder(toDouble() * SIConstants.kMega)
val Double.kilo get() = UnitBuilder(toDouble() * SIConstants.kKilo)
val Double.hecto get() = UnitBuilder(toDouble() * SIConstants.kHecto)
val Double.deca get() = UnitBuilder(toDouble() * SIConstants.kDeca)
// unit is omitted because scalar is 1
val Double.deci get() = UnitBuilder(toDouble() * SIConstants.kDeci)
val Double.centi get() = UnitBuilder(toDouble() * SIConstants.kCenti)
val Double.milli get() = UnitBuilder(toDouble() * SIConstants.kMilli)
val Double.micro get() = UnitBuilder(toDouble() * SIConstants.kMicro)
val Double.nano get() = UnitBuilder(toDouble() * SIConstants.kNano)
val Double.pico get() = UnitBuilder(toDouble() * SIConstants.kPico)
val Double.femto get() = UnitBuilder(toDouble() * SIConstants.kFemto)
val Double.atto get() = UnitBuilder(toDouble() * SIConstants.kAtto)
val Double.zepto get() = UnitBuilder(toDouble() * SIConstants.kZepto)
val Double.yocto get() = UnitBuilder(toDouble() * SIConstants.kYocto)



/**
 * Prefixless getters for all defined units.
 */
val Double.gram get() = SIUnit<Kilogram>(
    toDouble() / 1000.0
)
val Double.second get() = SIUnit<Second>(
    toDouble()
)
val Double.amp get() = SIUnit<Ampere>(
    toDouble()
)
val Double.coulomb get() = SIUnit<Coulomb>(
    toDouble()
)
val Double.volt get() = SIUnit<Volt>(
    toDouble()
)
val Double.ohm get() = SIUnit<Ohm>(
    toDouble()
)
val Double.newton get() = SIUnit<Newton>(
    toDouble()
)
val Double.joule get() = SIUnit<Joule>(
    toDouble()
)
val Double.hertz get() = SIUnit<Hertz>(
    toDouble()
)
val Double.curvature get() = SIUnit<Curvature>(
    toDouble()
)
val Double.meter get() = SIUnit<Meter>(
    toDouble()
)

val Double.radian get() = SIUnit<Radian>(
    toDouble()
)
val Double.degree get() = SIUnit<Radian>(
    Math.toRadians(toDouble())
)