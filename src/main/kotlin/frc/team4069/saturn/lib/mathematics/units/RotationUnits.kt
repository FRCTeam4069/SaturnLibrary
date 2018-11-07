package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.kEpsilon

val Number.radian get() = Rotation2d(toDouble())
val Number.degree get() = Math.toRadians(toDouble()).radian

class Rotation2d : SIUnit<Rotation2d> {

    override val value: Double
    val cos: Double
    val sin: Double

    constructor(value: Double) {
        this.value = value

        cos = Math.cos(value)
        sin = Math.sin(value)
    }

    constructor(x: Double, y: Double, normalize: Boolean) {
        if (normalize) {
            val magnitude = Math.hypot(x, y)
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
        value = Math.atan2(sin, cos)
    }

    val radian get() = value % (Math.PI * 2)
    val degree get() = Math.toDegrees(value)

    fun isParallel(rotation: Rotation2d) = (this - rotation).radian epsilonEquals 0.0

    override fun plus(other: Rotation2d): Rotation2d {
        return Rotation2d(
                cos * other.cos - sin * other.sin,
                cos * other.sin + sin * other.cos,
                true
        )
    }

    override fun minus(other: Rotation2d) = plus(-other)

    override fun createNew(newBaseValue: Double) = Rotation2d(newBaseValue)

    override fun toString(): String {
        return "Rotation2d($radian rad)"
    }

    companion object {
        val kRotation = 360.degree
    }
}