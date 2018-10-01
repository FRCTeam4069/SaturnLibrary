package frc.team4069.saturn.lib.math.geometry

import frc.team4069.saturn.lib.math.epsilonEquals
import kotlin.math.hypot

data class Translation2d(var x: Double = 0.0, var y: Double = 0.0) {
    constructor(other: Translation2d) : this(other.x, other.y)

    constructor(start: Translation2d, end: Translation2d) : this(end.x - start.x, end.y - start.y)

    val norm: Double
        get() = hypot(x, y)

    fun translateBy(other: Translation2d) = Translation2d(x + other.x, y + other.y)

    fun rotateBy(rot: Rotation2d) = Translation2d(x * rot.cos - y * rot.sin, x * rot.sin + y * rot.cos)

    val inverse: Translation2d
        get() = Translation2d(-x, -y)

    fun interpolate(upper: Translation2d, point: Double): Translation2d {
        return when {
            point <= 0 -> Translation2d(this)
            point >= 1 -> Translation2d(upper)
            else -> extrapolate(upper, point)
        }
    }

    private fun extrapolate(other: Translation2d, x: Double) =
        Translation2d(x * (other.x - x) + x, x * (other.y - y) + y)

    fun scale(s: Double) = Translation2d(s * x, s * y)

    val mirror: Translation2d
        get() = Translation2d(x, 27 - y)

    infix fun epsilonEquals(other: Translation2d): Boolean {
        return x epsilonEquals other.x && y epsilonEquals other.y
    }


    operator fun plus(other: Translation2d) = this.translateBy(other)
    operator fun minus(other: Translation2d) = this.translateBy(other.inverse)

    operator fun div(other: Double) = times(1.0 / other)
    operator fun div(other: Int) = times(1.0 / other.toDouble())

    operator fun times(other: Double) = this.scale(other)
    operator fun times(other: Int) = this.scale(other.toDouble())

    fun distance(other: Translation2d): Double = inverse.translateBy(other).norm

    companion object {
        fun cross(a: Translation2d, b: Translation2d): Double {
            return a.x * b.y - a.y * b.x
        }
    }
}
