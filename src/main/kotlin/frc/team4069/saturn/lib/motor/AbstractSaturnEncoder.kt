package frc.team4069.saturn.lib.motor

import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel

abstract class AbstractSaturnEncoder<T: SIUnit<T>>(val model: NativeUnitModel<T>) : SaturnEncoder<T> {
    override val position get() = model.fromNativeUnitPosition(rawPosition)
    override val velocity get() = model.fromNativeUnitVelocity(rawVelocity)
}