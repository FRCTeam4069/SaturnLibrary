package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.fractions.SIFrac12
import frc.team4069.saturn.lib.mathematics.units.second

typealias NativeUnitAcceleration = SIFrac12<NativeUnit, Time, Time>

fun <T : SIUnit<T>> SIFrac12<T, Time, Time>.fromModel(model: NativeUnitModel<T>): NativeUnitAcceleration =
        (this * 1.second).fromModel(model) / 1.second

fun <T : SIUnit<T>> NativeUnitAcceleration.toModel(model: NativeUnitModel<T>): SIFrac12<T, Time, Time> =
        (this * 1.second).toModel(model) / 1.second

val Number.STUPer100msPerSecond: NativeUnitAcceleration
    get() = STUPer100ms / 1.second

val NativeUnitAcceleration.STUPer100msPerSecond get() = (this * 1.second).STUPer100ms