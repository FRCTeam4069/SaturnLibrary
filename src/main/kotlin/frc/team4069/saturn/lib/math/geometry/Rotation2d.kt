package frc.team4069.saturn.lib.math.geometry

import frc.team4069.saturn.lib.math.epsilon
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

data class Rotation2d(var radians: Double) {
    val cos get() = cos(radians)
    val sin get() = sin(radians)

    val tan by lazy {
        if (cos.absoluteValue < epsilon) {
            if (sin >= 0.0) {

                Double.POSITIVE_INFINITY
            } else {
                Double.NEGATIVE_INFINITY
            }
        }else {
            sin / cos
        }
    }

    constructor() : this(0.0)

    val degrees = Math.toDegrees(radians)

    fun toTranslation() = Translation2d(cos, sin)
}