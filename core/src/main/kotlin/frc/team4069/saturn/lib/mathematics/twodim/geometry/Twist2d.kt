/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

package frc.team4069.saturn.lib.mathematics.twodim.geometry

import frc.team4069.saturn.lib.mathematics.kEpsilon
import frc.team4069.saturn.lib.mathematics.units.*
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class Twist2d(
        internal val _dx: Double,
        internal val _dy: Double,
        val dTheta: Rotation2d
) {

    val dx = _dx.meter
    val dy = _dy.meter

    constructor(dx: Length, dy: Length, dTheta: Rotation2d)
            : this(dx.value, dy.value, dTheta)

    constructor(dx: Length, dy: Length, dTheta: SIUnit<Radian>)
        : this(dx.value, dy.value, dTheta.toRotation2d())

    val norm = if (dy.value == 0.0) dx.absoluteValue else hypot(dx.value, dy.value).meter

    val asPose: Pose2d
        get() {
            val dTheta = this.dTheta.radian
            val sinTheta = sin(dTheta)
            val cosTheta = cos(dTheta)

            val (s, c) = if (dTheta.absoluteValue < kEpsilon) {
                1.0 - 1.0 / 6.0 * dTheta * dTheta to .5 * dTheta
            } else {
                sinTheta / dTheta to (1.0 - cosTheta) / dTheta
            }
            return Pose2d(
                    Translation2d(dx * s - dy * c, dx * c + dy * s),
                    Rotation2d(cosTheta, sinTheta, false)
            )
        }

    operator fun times(scale: Double) =
            Twist2d(dx * scale, dy * scale, dTheta * scale)
}