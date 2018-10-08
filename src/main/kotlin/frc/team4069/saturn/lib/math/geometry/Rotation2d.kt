package frc.team4069.saturn.lib.math.geometry

import frc.team4069.saturn.lib.math.EPSILON
import frc.team4069.saturn.lib.math.epsilonEquals
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class Rotation2d(x: Double = 1.0, y: Double = 0.0, normalize: Boolean = false) {

    val sin: Double
    val cos: Double

    init {
        if (normalize) {
            val magnitude = Math.hypot(x, y)
            if (magnitude > EPSILON) {
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
    }

    constructor(other: Rotation2d) : this(other.cos, other.sin, false)
    constructor(degrees: Double) : this(cos(Math.toRadians(degrees)), sin(Math.toRadians(degrees)), false)
    constructor(direction: Translation2d, normalize: Boolean) : this(direction.x, direction.y, normalize)

    val tan by lazy {
        if (abs(cos) < EPSILON) {
            if (sin >= 0.0) {
                Double.POSITIVE_INFINITY
            } else {
                Double.NEGATIVE_INFINITY
            }
        } else {
            sin / cos
        }
    }

    val radians = Math.atan2(sin, cos)

    val degrees = Math.toDegrees(radians)

    val normal = Rotation2d(-sin, cos, false)

    val inverse = Rotation2d(cos, -sin, false)

    fun rotateBy(other: Rotation2d) = Rotation2d(
        cos * other.cos - sin * other.sin,
        cos * other.sin + sin * other.cos, true
    )

    fun isParallel(other: Rotation2d) =
        Translation2d.cross(toTranslation(), other.toTranslation()) epsilonEquals 0.0

    fun toTranslation() = Translation2d(cos, sin)


    operator fun plus(other: Rotation2d) = rotateBy(other)
    operator fun minus(other: Rotation2d) = rotateBy(other.inverse)

    fun interpolate(upper: Rotation2d, point: Double): Rotation2d {
        if (point <= 0) {
            return Rotation2d(this)
        } else if (point >= 1) {
            return Rotation2d(upper)
        }

        val angleDiff = inverse.rotateBy(upper).radians
        return this.rotateBy(Rotation2d.fromRadians(angleDiff * point))
    }

    override fun toString() = "Rotation2d(x=$cos, y=$sin)"
    override fun equals(other: Any?): Boolean {
        return other is Rotation2d && distance(other) < EPSILON
    }

    fun distance(other: Rotation2d): Double = inverse.rotateBy(other).radians

    companion object {
        private val id = Rotation2d()

        fun identity() = id

        fun fromRadians(rads: Double) = Rotation2d(cos(rads), sin(rads))
        fun fromDegrees(deg: Double) = Rotation2d(deg)
    }
}