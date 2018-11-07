/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

package frc.team4069.saturn.lib.mathematics.twodim.geometry

import frc.team4069.saturn.lib.mathematics.kEpsilon
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.Rotation2d
import frc.team4069.saturn.lib.mathematics.units.meter

class Twist2d(
    val dx: Length,
    val dy: Length,
    val dTheta: Rotation2d
) {

    val norm
        get() = if (dy.value == 0.0) dx.absoluteValue else Math.hypot(dx.value, dy.value).meter

    val asPose: Pose2d
        get() {
            val dTheta = this.dTheta.radian
            val sinTheta = Math.sin(dTheta)
            val cosTheta = Math.cos(dTheta)

            val (s, c) = if (Math.abs(dTheta) < kEpsilon) {
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