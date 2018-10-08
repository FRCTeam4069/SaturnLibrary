package frc.team4069.saturn.lib.math.uom.distance

import frc.team4069.saturn.lib.math.uom.UnitPreferences

class Inches(override val `in`: Double, override val settings: UnitPreferences) : DistanceUnit {
    override val stu: Int
        get() = Feet(ft, settings).stu
    override val ft: Double
        get() = `in` / 12.0
}