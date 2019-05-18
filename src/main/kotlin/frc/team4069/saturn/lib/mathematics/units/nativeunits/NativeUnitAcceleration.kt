package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.derivedunits.Acceleration
import frc.team4069.saturn.lib.mathematics.units.second

typealias NativeUnitAcceleration = Acceleration<NativeUnit>

val Number.STUPer100msPerSecond: NativeUnitAcceleration
    get() = STUPer100ms / 1.second

val NativeUnitAcceleration.STUPer100msPerSecond get() = (this * 1.second).STUPer100ms