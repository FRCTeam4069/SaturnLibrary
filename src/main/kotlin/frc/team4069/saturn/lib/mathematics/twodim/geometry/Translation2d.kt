/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

@file:Suppress("KDocUnresolvedReference", "EqualsOrHashCode")

package frc.team4069.saturn.lib.mathematics.twodim.geometry

import frc.team4069.saturn.lib.mathematics.lerp
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.conversions.meter
import frc.team4069.saturn.lib.mathematics.units.meter
import frc.team4069.saturn.lib.types.VaryInterpolatable
import kotlin.math.hypot

fun Rotation2d.toTranslation() = Translation2d(cos, sin)

data class Translation2d(
    var xRaw: Double = 0.0,
    var yRaw: Double = 0.0
) : VaryInterpolatable<Translation2d> {

    var x: Length
        get() = xRaw.meter
        set(value) {
            xRaw = value.meter
        }

    var y: Length
        get() = yRaw.meter
        set(value) {
            yRaw = value.meter
        }

    constructor(
        x: Length,
        y: Length
    ) : this(
            x.meter,
            y.meter
    )

    val norm
        get() = hypot(xRaw, yRaw)

    override fun interpolate(endValue: Translation2d, t: Double) = when {
        t <= 0 -> this
        t >= 1 -> endValue
        else -> Translation2d(
                xRaw.lerp(endValue.xRaw, t),
                yRaw.lerp(endValue.yRaw, t)
        )
    }

    override fun distance(other: Translation2d) = (-this + other).norm

    operator fun plus(other: Translation2d) = Translation2d(
            xRaw + other.xRaw,
            yRaw + other.yRaw
    )

    operator fun minus(other: Translation2d) = Translation2d(
            xRaw - other.xRaw,
            yRaw - other.yRaw
    )

    operator fun times(other: Rotation2d) = Translation2d(
            xRaw * other.cos - yRaw * other.sin,
            xRaw * other.sin + yRaw * other.cos
    )

    operator fun times(other: Number): Translation2d {
        val factor = other.toDouble()
        return Translation2d(
                xRaw * factor,
                yRaw * factor
        )
    }

    operator fun div(other: Number): Translation2d {
        val factor = other.toDouble()
        return Translation2d(
                xRaw / factor,
                yRaw / factor
        )
    }

    operator fun unaryMinus() = Translation2d(-xRaw, -yRaw)

    companion object {
        fun cross(a: Translation2d, b: Translation2d) = a.xRaw * b.yRaw - a.yRaw * b.xRaw
    }
}