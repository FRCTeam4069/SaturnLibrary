package frc.team4069.saturn.lib.mathematics.units.conversions

import frc.team4069.saturn.lib.mathematics.units.Meter
import frc.team4069.saturn.lib.mathematics.units.Radian
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.derived.Velocity
import frc.team4069.saturn.lib.mathematics.units.meter

typealias LinearVelocity = Velocity<Meter>
typealias AngularVelocity = Velocity<Radian>

private val meterToFeet = 1.meter.feet
private val meterToInches = 1.meter.inch
private val secondsPerMinute = 1.minute.second

val SIUnit<LinearVelocity>.feetPerSecond get() = value * meterToFeet
val SIUnit<LinearVelocity>.feetPerMinute get() = feetPerSecond * secondsPerMinute
val SIUnit<LinearVelocity>.inchesPerSecond get() = value * meterToInches
