package frc.team4069.saturn.lib.math.uom.velocity

import frc.team4069.saturn.lib.math.TAU
import frc.team4069.saturn.lib.math.uom.UnitPreferences

class FeetPerSecond(override val fps: Double, override val settings: UnitPreferences) : VelocityUnit {
    override val stu: Int
        get() = ((fps * 6.0 * settings.sensorUnitsPerRotation) / (5 * TAU * settings.radius)).toInt()
    override val ips: Double
        get() = fps * 12.0

}