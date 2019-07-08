package frc.team4069.saturn.lib.mathematics.units.conversions

import frc.team4069.saturn.lib.mathematics.units.Ampere
import frc.team4069.saturn.lib.mathematics.units.SIConstants
import frc.team4069.saturn.lib.mathematics.units.SIUnit

val SIUnit<Ampere>.yottaamp get() = value / SIConstants.kYotta
val SIUnit<Ampere>.zettaamp get() = value / SIConstants.kZetta
val SIUnit<Ampere>.exaamp get() = value / SIConstants.kExa
val SIUnit<Ampere>.petaamp get() = value / SIConstants.kPeta
val SIUnit<Ampere>.teraamp get() = value / SIConstants.kTera
val SIUnit<Ampere>.gigaamp get() = value / SIConstants.kGiga
val SIUnit<Ampere>.megaamp get() = value / SIConstants.kMega
val SIUnit<Ampere>.kiloamp get() = value / SIConstants.kKilo
val SIUnit<Ampere>.hectoamp get() = value / SIConstants.kHecto
val SIUnit<Ampere>.decaamp get() = value / SIConstants.kDeca
val SIUnit<Ampere>.amp get() = value
val SIUnit<Ampere>.deciamp get() = value / SIConstants.kDeci
val SIUnit<Ampere>.centiamp get() = value / SIConstants.kCenti
val SIUnit<Ampere>.milliamp get() = value / SIConstants.kMilli
val SIUnit<Ampere>.microamp get() = value / SIConstants.kMicro
val SIUnit<Ampere>.nanoamp get() = value / SIConstants.kNano
val SIUnit<Ampere>.picoamp get() = value / SIConstants.kPico
val SIUnit<Ampere>.femtoamp get() = value / SIConstants.kFemto
val SIUnit<Ampere>.attoamp get() = value / SIConstants.kAtto
val SIUnit<Ampere>.zeptoamp get() = value / SIConstants.kZepto
val SIUnit<Ampere>.yoctoamp get() = value / SIConstants.kYocto
