package frc.team4069.saturn.lib.math.uom.velocity

import frc.team4069.saturn.lib.math.TAU
import frc.team4069.saturn.lib.math.uom.UnitPreferences

class NativeUnitsPerSecond(override val stu: Int, override val settings: UnitPreferences) : VelocityUnit {
    override val fps: Double
        get() = (stu.toDouble() / settings.sensorUnitsPerRotation.toDouble() * (TAU * settings.radius) / 12.0)
    override val ips: Double
        get() = fps * 12.0

}