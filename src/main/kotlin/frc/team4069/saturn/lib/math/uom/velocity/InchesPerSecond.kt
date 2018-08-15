package frc.team4069.saturn.lib.math.uom.velocity

import frc.team4069.saturn.lib.math.uom.UnitPreferences

class InchesPerSecond(override val ips: Double, override val settings: UnitPreferences) : VelocityUnit {
    override val stu: Int
        get() = FeetPerSecond(fps, settings).stu

    override val fps: Double
        get() = ips / 12.0

}