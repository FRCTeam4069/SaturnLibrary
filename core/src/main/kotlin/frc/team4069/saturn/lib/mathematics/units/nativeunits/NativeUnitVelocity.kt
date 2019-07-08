package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.derived.VelocityT

typealias NativeUnitVelocity = SIUnit<VelocityT<NativeUnit>>

val Number.STUPer100ms: NativeUnitVelocity get() = STU / 100.milli.second

val NativeUnitVelocity.STUPer100ms get() = value / 10
