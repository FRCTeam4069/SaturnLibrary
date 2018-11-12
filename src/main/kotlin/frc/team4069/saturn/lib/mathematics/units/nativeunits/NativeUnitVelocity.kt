package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.millisecond

typealias NativeUnitVelocity = Velocity<NativeUnit>

val Number.STUPer100ms: NativeUnitVelocity get() = STU / 100.millisecond

operator fun NativeUnit.div(other: Time) = NativeUnitVelocity(value / other.value, this)

val NativeUnitVelocity.STUPer100ms get() = value / 10

fun <T : SIUnit<T>> Velocity<T>.fromModel(model: NativeUnitModel<T>): NativeUnitVelocity =
    model.fromModel(type.createNew(value)).velocity

fun <T : SIUnit<T>> NativeUnitVelocity.toModel(model: NativeUnitModel<T>) =
    model.toModel(type.createNew(value)).velocity