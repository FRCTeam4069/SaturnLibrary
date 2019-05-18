package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.SIUnit

val Number.STU get() = NativeUnit(toDouble())

class NativeUnit(
    override val value: Double
) : SIUnit<NativeUnit> {
    override fun createNew(newValue: Double) = NativeUnit(newValue)
}