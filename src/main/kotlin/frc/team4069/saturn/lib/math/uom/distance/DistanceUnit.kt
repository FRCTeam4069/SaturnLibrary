package frc.team4069.saturn.lib.math.uom.distance

import frc.team4069.saturn.lib.math.uom.UnitPreferences
import kotlin.math.roundToInt

interface DistanceUnit {
    val stu: Int
    val ft: Double
    val `in`: Double

    val settings: UnitPreferences

    operator fun plus(other: DistanceUnit) = NativeUnits(this.stu + other.stu, settings)
    operator fun minus(other: DistanceUnit) = NativeUnits(this.stu - other.stu, settings)
    operator fun times(other: DistanceUnit) = NativeUnits(this.stu * other.stu, settings)
    operator fun div(other: DistanceUnit) = NativeUnits(this.stu / other.stu, settings)
    operator fun times(scalar: Double) = Feet(this.ft * scalar, settings)
    operator fun div(scalar: Double) = Feet(this.ft / scalar, settings)
    operator fun unaryMinus() = NativeUnits(-this.stu, settings)
}

val preferences = UnitPreferences(256, 8.0)

val Int.stu
    get() = NativeUnits(this, preferences)

val Int.`in`
    get() = Inches(this.toDouble(), preferences)

val Int.ft
    get() = Feet(this.toDouble(), preferences)

val Double.stu
    get() = NativeUnits(this.roundToInt(), preferences)

val Double.`in`
    get() = Inches(this, preferences)

val Double.ft
    get() = Inches(this, preferences)
