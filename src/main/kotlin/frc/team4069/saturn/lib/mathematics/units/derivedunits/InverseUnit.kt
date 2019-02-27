package frc.team4069.saturn.lib.mathematics.units.derivedunits

import frc.team4069.saturn.lib.mathematics.units.*

typealias Curvature = InverseUnit<Length>
typealias Frequency = InverseUnit<Time>

val Number.curvature get() = Curvature(toDouble())

val Int.yottahertz get() = Frequency(toDouble() * SIConstants.kYotta)
val Int.zettahertz get() = Frequency(toDouble() * SIConstants.kZetta)
val Int.exahertz get() = Frequency(toDouble() * SIConstants.kExa)
val Int.petahertz get() = Frequency(toDouble() * SIConstants.kPeta)
val Int.terahertz get() = Frequency(toDouble() * SIConstants.kTera)
val Int.gigahertz get() = Frequency(toDouble() * SIConstants.kGiga)
val Int.megahertz get() = Frequency(toDouble() * SIConstants.kMega)
val Int.kilohertz get() = Frequency(toDouble() * SIConstants.kKilo)
val Int.hectohertz get() = Frequency(toDouble() * SIConstants.kHecto)
val Int.decahertz get() = Frequency(toDouble() * SIConstants.kDeca)
val Int.hertz get() = Frequency(toDouble())
val Int.decihertz get() = Frequency(toDouble() * SIConstants.kDeci)
val Int.centihertz get() = Frequency(toDouble() * SIConstants.kCenti)
val Int.millihertz get() = Frequency(toDouble() * SIConstants.kMilli)
val Int.microhertz get() = Frequency(toDouble() * SIConstants.kMicro)
val Int.nanohertz get() = Frequency(toDouble() * SIConstants.kNano)
val Int.picohertz get() = Frequency(toDouble() * SIConstants.kPico)
val Int.femtohertz get() = Frequency(toDouble() * SIConstants.kFemto)
val Int.attohertz get() = Frequency(toDouble() * SIConstants.kAtto)
val Int.zeptohertz get() = Frequency(toDouble() * SIConstants.kZepto)
val Int.yoctohertz get() = Frequency(toDouble() * SIConstants.kYocto)

@Suppress("FunctionName")
fun Curvature(value: Double) = Curvature(value, 0.meter)

@Suppress("FunctionName")
fun Frequency(value: Double) = Frequency(value, 0.second)

operator fun <T : SIUnit<T>> Number.div(other: T): InverseUnit<T> =
        InverseUnit(this.toDouble(), other)

class InverseUnit<T : SIUnit<T>>(
        override val value: Double,
        val type: T
) : SIValue<InverseUnit<T>> {
    override fun createNew(newValue: Double) = InverseUnit(newValue, type)
}

val Frequency.yottahertz get() = value / SIConstants.kYotta
val Frequency.zettahertz get() = value / SIConstants.kZetta
val Frequency.exahertz get() = value / SIConstants.kExa
val Frequency.petahertz get() = value / SIConstants.kPeta
val Frequency.terahertz get() = value / SIConstants.kTera
val Frequency.gigahertz get() = value / SIConstants.kGiga
val Frequency.megahertz get() = value / SIConstants.kMega
val Frequency.kilohertz get() = value / SIConstants.kKilo
val Frequency.hectohertz get() = value / SIConstants.kHecto
val Frequency.decahertz get() = value / SIConstants.kDeca
val Frequency.hertz get() = value
val Frequency.decihertz get() = value / SIConstants.kDeci
val Frequency.centihertz get() = value / SIConstants.kCenti
val Frequency.millihertz get() = value / SIConstants.kMilli
val Frequency.microhertz get() = value / SIConstants.kMicro
val Frequency.nanohertz get() = value / SIConstants.kNano
val Frequency.picohertz get() = value / SIConstants.kPico
val Frequency.femtohertz get() = value / SIConstants.kFemto
val Frequency.attohertz get() = value / SIConstants.kAtto
val Frequency.zeptohertz get() = value / SIConstants.kZepto
val Frequency.yoctohertz get() = value / SIConstants.kYocto
