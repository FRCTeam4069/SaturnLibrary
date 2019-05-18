package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity
import frc.team4069.saturn.lib.mathematics.units.millisecond

typealias NativeUnitVelocity = Velocity<NativeUnit>

val Number.STUPer100ms: NativeUnitVelocity get() = STU / 100.millisecond

operator fun NativeUnit.div(other: Time) = NativeUnitVelocity(value / other.value, this)

val NativeUnitVelocity.STUPer100ms get() = value / 10
