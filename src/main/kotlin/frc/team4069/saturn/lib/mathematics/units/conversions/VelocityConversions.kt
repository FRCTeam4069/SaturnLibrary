package frc.team4069.saturn.lib.mathematics.units.conversions

import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.derived.VelocityT

typealias LinearVelocityT = VelocityT<Meter>
typealias AngularVelocityT = VelocityT<Radian>

private val meterToFeet = 1.meter.feet
private val meterToInches = 1.meter.inch
private val secondsPerMinute = 1.minute.second

val SIUnit<LinearVelocityT>.feetPerSecond get() = value * meterToFeet
val SIUnit<LinearVelocityT>.feetPerMinute get() = feetPerSecond * secondsPerMinute
val SIUnit<LinearVelocityT>.inchesPerSecond get() = value * meterToInches
