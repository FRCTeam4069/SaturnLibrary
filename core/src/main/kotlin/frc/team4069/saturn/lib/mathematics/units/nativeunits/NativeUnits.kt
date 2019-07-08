package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.NativeUnit
import frc.team4069.saturn.lib.mathematics.units.SIUnit

val Number.STU get() = SIUnit<NativeUnit>(toDouble())