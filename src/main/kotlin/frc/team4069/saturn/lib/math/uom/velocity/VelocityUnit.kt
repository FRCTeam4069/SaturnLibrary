package frc.team4069.saturn.lib.math.uom.velocity

import frc.team4069.saturn.lib.math.uom.UnitPreferences

interface VelocityUnit {
    val stu: Int
    val fps: Double
    val ips: Double

    val settings: UnitPreferences
}