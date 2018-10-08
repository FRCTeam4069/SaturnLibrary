package frc.team4069.saturn.lib.math.uom.distance

import frc.team4069.saturn.lib.math.TAU
import frc.team4069.saturn.lib.math.uom.UnitPreferences

class NativeUnits(override val stu: Int, override val settings: UnitPreferences) : DistanceUnit {
    override val ft: Double
        get() = stu.toDouble() / settings.sensorUnitsPerRotation.toDouble() * (TAU * settings.radius) / 12.0
    override val `in`: Double
        get() = ft * 12.0

}