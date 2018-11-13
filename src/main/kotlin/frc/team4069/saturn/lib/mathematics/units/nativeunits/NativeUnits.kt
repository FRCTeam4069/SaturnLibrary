package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.SIUnit

fun <T : SIUnit<T>> T.fromModel(model: NativeUnitModel<T>) = model.fromModel(this)

val Number.STU get() = NativeUnit(toDouble())

class NativeUnit(
    override val value: Double
) : SIUnit<NativeUnit> {
    override fun createNew(newValue: Double) = NativeUnit(newValue)

    fun <T : SIUnit<T>> toModel(model: NativeUnitModel<T>) = model.toModel(this)
}