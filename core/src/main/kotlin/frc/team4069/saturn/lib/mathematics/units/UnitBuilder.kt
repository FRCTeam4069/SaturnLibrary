package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.units.derived.*

val <T : Key> SIUnit<T>.velocity get() = SIUnit<Velocity<T>>(
        value
)
val <T : Key> SIUnit<T>.acceleration get() = SIUnit<Acceleration<T>>(
        value
)

/**
 * A generic class to construct any unit with the given prefix scalar
 *
 * An instance of toDouble() class will be constructed by the unitless prefixes defined below, at which point a unit can be chosen with the given prefix.
 */
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS") //May change if Kotlin changes
inline class UnitBuilder internal constructor(val value: Double) {
    val gram get() = SIUnit<Kilogram>(
            value / 1000.0
    )
    val second get() = SIUnit<Second>(
            value
    )
    val amp get() = SIUnit<Ampere>(
            value
    )
    val coulomb get() = SIUnit<Coulomb>(
            value
    )
    val volt get() = SIUnit<Volt>(
            value
    )
    val ohm get() = SIUnit<Ohm>(
            value
    )
    val newton get() = SIUnit<Newton>(
            value
    )
    val joule get() = SIUnit<Joule>(
            value
    )
    val hertz get() = SIUnit<Hertz>(
            value
    )
    val curvature get() = SIUnit<Curvature>(
            value
    )
    val meter get() = SIUnit<Meter>(
            value
    )

    val radian get() = SIUnit<Radian>(
            value
    )
    val degree get() = SIUnit<Radian>(
            Math.toRadians(value)
    )
}

/**
 * Prefixless getters for SI constants.
 */
val Number.yotta get() = UnitBuilder(toDouble() * SIConstants.kYotta)
val Number.zetta get() = UnitBuilder(toDouble() * SIConstants.kZetta)
val Number.exa get() = UnitBuilder(toDouble() * SIConstants.kExa)
val Number.peta get() = UnitBuilder(toDouble() * SIConstants.kPeta)
val Number.tera get() = UnitBuilder(toDouble() * SIConstants.kTera)
val Number.giga get() = UnitBuilder(toDouble() * SIConstants.kGiga)
val Number.mega get() = UnitBuilder(toDouble() * SIConstants.kMega)
val Number.kilo get() = UnitBuilder(toDouble() * SIConstants.kKilo)
val Number.hecto get() = UnitBuilder(toDouble() * SIConstants.kHecto)
val Number.deca get() = UnitBuilder(toDouble() * SIConstants.kDeca)
// unit is omitted because scalar is 1
val Number.deci get() = UnitBuilder(toDouble() * SIConstants.kDeci)
val Number.centi get() = UnitBuilder(toDouble() * SIConstants.kCenti)
val Number.milli get() = UnitBuilder(toDouble() * SIConstants.kMilli)
val Number.micro get() = UnitBuilder(toDouble() * SIConstants.kMicro)
val Number.nano get() = UnitBuilder(toDouble() * SIConstants.kNano)
val Number.pico get() = UnitBuilder(toDouble() * SIConstants.kPico)
val Number.femto get() = UnitBuilder(toDouble() * SIConstants.kFemto)
val Number.atto get() = UnitBuilder(toDouble() * SIConstants.kAtto)
val Number.zepto get() = UnitBuilder(toDouble() * SIConstants.kZepto)
val Number.yocto get() = UnitBuilder(toDouble() * SIConstants.kYocto)



/**
 * Prefixless getters for all defined units.
 */
val Number.gram get() = SIUnit<Kilogram>(
        toDouble() / 1000.0
)
val Number.second get() = SIUnit<Second>(
        toDouble()
)
val Number.amp get() = SIUnit<Ampere>(
        toDouble()
)
val Number.coulomb get() = SIUnit<Coulomb>(
        toDouble()
)
val Number.volt get() = SIUnit<Volt>(
        toDouble()
)
val Number.ohm get() = SIUnit<Ohm>(
        toDouble()
)
val Number.newton get() = SIUnit<Newton>(
        toDouble()
)
val Number.joule get() = SIUnit<Joule>(
        toDouble()
)
val Number.hertz get() = SIUnit<Hertz>(
        toDouble()
)
val Number.curvature get() = SIUnit<Curvature>(
        toDouble()
)
val Number.meter get() = SIUnit<Meter>(
        toDouble()
)

val Number.radian get() = SIUnit<Radian>(
        toDouble()
)
val Number.degree get() = SIUnit<Radian>(
        Math.toRadians(toDouble())
)