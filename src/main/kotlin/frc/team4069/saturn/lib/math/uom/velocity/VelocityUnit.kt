package frc.team4069.saturn.lib.math.uom.velocity

import frc.team4069.saturn.lib.math.uom.UnitPreferences
import frc.team4069.saturn.lib.math.uom.distance.preferences

interface VelocityUnit {
    val stu: Int
    val fps: Double
    val ips: Double

    val settings: UnitPreferences
}

val Int.stups: VelocityUnit
    get() = NativeUnitsPerSecond(this, preferences)

val Int.fps: VelocityUnit
    get() = FeetPerSecond(this.toDouble(), preferences)

val Int.ips: VelocityUnit
    get() = InchesPerSecond(this.toDouble(), preferences)

val Double.stups: VelocityUnit
    get() = NativeUnitsPerSecond(this.toInt(), preferences)

val Double.fps: VelocityUnit
    get() = FeetPerSecond(this, preferences)

val Double.ips: VelocityUnit
    get() = InchesPerSecond(this, preferences)