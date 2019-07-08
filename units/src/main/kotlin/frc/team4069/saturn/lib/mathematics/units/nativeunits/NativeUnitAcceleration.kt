package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.derived.Acceleration

typealias NativeUnitAcceleration = Acceleration<NativeUnit>

val Number.STUPer100msPerSecond: SIUnit<NativeUnitAcceleration>
    get() = STUPer100ms / 1.second

val SIUnit<NativeUnitAcceleration>.STUPer100msPerSecond get() = (this * 1.second).STUPer100ms
