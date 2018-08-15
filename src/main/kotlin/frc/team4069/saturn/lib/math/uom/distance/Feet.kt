package frc.team4069.saturn.lib.math.uom.distance

import frc.team4069.saturn.lib.math.TAU
import frc.team4069.saturn.lib.math.uom.UnitPreferences
import kotlin.math.roundToInt

class Feet(override val ft: Double, override val settings: UnitPreferences) : DistanceUnit {
    override val stu: Int
        get() = (ft * 12.0 / (TAU * settings.radius) * settings.sensorUnitsPerRotation.toDouble()).roundToInt()

    override val `in`: Double
        get() = ft * 12.0

}