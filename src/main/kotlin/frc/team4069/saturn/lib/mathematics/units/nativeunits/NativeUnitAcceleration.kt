package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.second
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.acceleration

typealias NativeUnitAcceleration = Acceleration<NativeUnit>

fun <T : SIUnit<T>> Acceleration<T>.fromModel(model: NativeUnitModel<T>): NativeUnitAcceleration =
    model.fromModel(type.createNew(value)).acceleration

fun <T : SIUnit<T>> NativeUnitAcceleration.toModel(model: NativeUnitModel<T>): Acceleration<T> =
    model.toModel(type.createNew(value)).acceleration

val Number.STUPer100msPerSecond: NativeUnitAcceleration
    get() = STUPer100ms / 1.second

val NativeUnitAcceleration.STUPer100msPerSecond get() = (this * 1.second).STUPer100ms