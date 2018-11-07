package frc.team4069.saturn.lib.mathematics.units.derivedunits

import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.SIValue

operator fun <T : SIUnit<T>> Number.div(other: T): InverseUnit<T> =
        InverseUnit(this.toDouble(), other)

class InverseUnit<T : SIUnit<T>>(
    override val value: Double,
    val type: T
) : SIValue<InverseUnit<T>> {
    override fun createNew(newValue: Double) = InverseUnit(newValue, type)
}