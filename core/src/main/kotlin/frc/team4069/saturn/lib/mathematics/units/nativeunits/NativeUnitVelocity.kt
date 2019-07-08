package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.derived.Velocity

typealias NativeUnitVelocity = Velocity<NativeUnit>

val Number.STUPer100ms: SIUnit<NativeUnitVelocity> get() = STU / 100.milli.second

val SIUnit<NativeUnitVelocity>.STUPer100ms get() = value / 10
