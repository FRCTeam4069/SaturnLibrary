/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

@file:Suppress("unused", "EqualsOrHashCode")

package frc.team4069.saturn.lib.mathematics.twodim.geometry

import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.kEpsilon
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.feet
import frc.team4069.saturn.lib.mathematics.units.conversions.meter
import frc.team4069.saturn.lib.types.VaryInterpolatable
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

// Stores a pose on the field. Contains a translation and a rotation

data class Pose2d(
    val translation: Translation2d = Translation2d(),
    val rotation: Rotation2d = Rotation2d(0.0)
) : VaryInterpolatable<Pose2d> {

    infix fun fuzzyEquals(other: Pose2d): Boolean {
        return translation.x epsilonEquals other.translation.x &&
                translation.y epsilonEquals other.translation.y &&
                rotation.radian epsilonEquals other.rotation.radian
    }

    constructor(
        x: SIUnit<Meter>,
        y: SIUnit<Meter>,
        rotation: Rotation2d = Rotation2d(0.0)
    ) : this(Translation2d(x, y), rotation)

    constructor(
            x: SIUnit<Meter>,
            y: SIUnit<Meter>,
            rotation: SIUnit<Radian> = 0.radian
    ) : this(Translation2d(x, y), rotation.toRotation2d())

    val twist: Twist2d
        get() {
            val dtheta = rotation.radian
            val halfDTheta = dtheta / 2.0
            val cosMinusOne = rotation.cos - 1.0

            val halfThetaByTanOfHalfDTheta = if (cosMinusOne.absoluteValue < kEpsilon) {
                1.0 - 1.0 / 12.0 * dtheta * dtheta
            } else {
                -(halfDTheta * rotation.sin) / cosMinusOne
            }
            val translationPart = translation *
                    Rotation2d(halfThetaByTanOfHalfDTheta, -halfDTheta, false)
            return Twist2d(translationPart.x, translationPart.y, rotation)
        }

    val mirror
        get() = Pose2d(Translation2d(translation.x, 27.feet - translation.y), -rotation)

    infix fun inFrameOfReferenceOf(fieldRelativeOrigin: Pose2d) = (-fieldRelativeOrigin) + this

    operator fun plus(other: Pose2d) = transformBy(other)

    fun transformBy(other: Pose2d) =
            Pose2d(
                    translation + (other.translation * rotation),
                    rotation + other.rotation
            )

    fun exp(twist: Twist2d): Pose2d {
        val dx = twist.dx.value
        val dy = twist.dy.value
        val dtheta = twist.dTheta.value

        val sinTheta = sin(dtheta)
        val cosTheta = cos(dtheta)

        val s: Double
        val c: Double
        if(dtheta.absoluteValue < kEpsilon) {
            s = 1.0 - 1.0 / 6.0 * dtheta.pow(2)
            c = 0.5 * dtheta
        } else {
            s = sinTheta / dtheta
            c = (1 - cosTheta) / dtheta
        }

        val transform = Pose2d(Translation2d((dx * s - dy * c).meter, (dx * c + dy * s).meter))
        return this + transform
    }

    operator fun unaryMinus(): Pose2d {
        val invertedRotation = -rotation
        return Pose2d((-translation) * invertedRotation, invertedRotation)
    }

    fun isCollinear(other: Pose2d): Boolean {
        if (!rotation.isParallel(other.rotation)) return false
        val twist = (-this + other).twist
        return twist.dy.value epsilonEquals 0.0 && twist.dTheta.value epsilonEquals 0.0
    }

    override fun interpolate(endValue: Pose2d, t: Double): Pose2d {
        if (t <= 0) return Pose2d(this.translation, this.rotation)
        else if (t >= 1) return Pose2d(endValue.translation, endValue.rotation)
        val twist = (-this + endValue).twist
        return this + (twist * t).asPose
    }

    override fun distance(other: Pose2d) = (-this + other).twist.norm.value
}