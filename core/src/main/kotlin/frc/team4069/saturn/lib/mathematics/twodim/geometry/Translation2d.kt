/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

@file:Suppress("KDocUnresolvedReference", "EqualsOrHashCode")

package frc.team4069.saturn.lib.mathematics.twodim.geometry

import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.types.VaryInterpolatable

fun Rotation2d.toTranslation() = Translation2d(cos.meter, sin.meter)

data class Translation2d(
        var x: SIUnit<Meter> = 0.meter,
        var y: SIUnit<Meter> = 0.meter
) : VaryInterpolatable<Translation2d> {

    val norm
        get() = hypot(x, y).value

    override fun interpolate(endValue: Translation2d, t: Double) = when {
        t <= 0 -> this
        t >= 1 -> endValue
        else -> Translation2d(
                x.lerp(endValue.x, t),
                y.lerp(endValue.y, t)
        )
    }

    override fun distance(other: Translation2d) = (-this + other).norm

    operator fun plus(other: Translation2d) = Translation2d(
            x + other.x,
            y + other.y
    )

    operator fun minus(other: Translation2d) = Translation2d(
            x - other.x,
            y - other.y
    )

    operator fun times(other: Rotation2d) = Translation2d(
            x * other.cos - y * other.sin,
            x * other.sin + y * other.cos
    )

    operator fun times(other: Number): Translation2d {
        val factor = other.toDouble()
        return Translation2d(
                x * factor,
                y * factor
        )
    }

    operator fun div(other: Number): Translation2d {
        val factor = other.toDouble()
        return Translation2d(
                x / factor,
                y / factor
        )
    }

    operator fun unaryMinus() = Translation2d(-x, -y)

    companion object {
        fun cross(a: Translation2d, b: Translation2d) = a.x * b.y - a.y * b.x
    }
}