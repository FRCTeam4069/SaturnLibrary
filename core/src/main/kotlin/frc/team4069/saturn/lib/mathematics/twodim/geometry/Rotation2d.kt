package frc.team4069.saturn.lib.mathematics.twodim.geometry

import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.kEpsilon
import frc.team4069.saturn.lib.mathematics.units.Radian
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

fun SIUnit<Radian>.toRotation2d() = Rotation2d(value)

class Rotation2d : Comparable<Rotation2d> {
    val value: Double
    val cos: Double
    val sin: Double

    constructor() : this(0.0)

    constructor(value: Double) : this(cos(value), sin(value), true)

    constructor(x: Double, y: Double, normalize: Boolean) {
        if (normalize) {
            val magnitude = hypot(x, y)
            if (magnitude > kEpsilon) {
                sin = y / magnitude
                cos = x / magnitude
            } else {
                sin = 0.0
                cos = 1.0
            }
        } else {
            cos = x
            sin = y
        }
        value = atan2(sin, cos)
    }

    fun toSI() = SIUnit<Radian>(value)

    val radian get() = value
    val degree get() = Math.toDegrees(value)

    fun isParallel(rotation: Rotation2d) = (this - rotation).radian epsilonEquals 0.0

    operator fun minus(other: Rotation2d) = plus(-other)
    operator fun unaryMinus() = Rotation2d(-value)

    operator fun times(other: Double) = Rotation2d(value * other)

    operator fun plus(other: Rotation2d): Rotation2d {
        return Rotation2d(
                cos * other.cos - sin * other.sin,
                cos * other.sin + sin * other.cos,
                true
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is Rotation2d && other.value epsilonEquals value
    }

    override fun compareTo(other: Rotation2d): Int {
        return value.compareTo(other.value)
    }

    companion object {
        fun fromDegrees(x: Double) = Rotation2d(Math.toRadians(x))
    }
}
