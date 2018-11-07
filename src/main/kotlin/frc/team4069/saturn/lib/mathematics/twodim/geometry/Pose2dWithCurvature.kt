/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

@file:Suppress("unused", "EqualsOrHashCode")

package frc.team4069.saturn.lib.mathematics.twodim.geometry

import frc.team4069.saturn.lib.mathematics.lerp
import frc.team4069.saturn.lib.types.Interpolatable
import frc.team4069.saturn.lib.types.VaryInterpolatable

data class Pose2dWithCurvature(
    val pose: Pose2d,
    val curvature: Curvature
) : VaryInterpolatable<Pose2dWithCurvature> {

    val mirror
        get() = Pose2dWithCurvature(
                pose.mirror,
                -curvature
        )

    override fun interpolate(endValue: Pose2dWithCurvature, t: Double) =
            Pose2dWithCurvature(
                    pose.interpolate(endValue.pose, t),
                    curvature.interpolate(endValue.curvature, t)
            )

    override fun distance(other: Pose2dWithCurvature) = pose.distance(other.pose)

    operator fun plus(other: Pose2d) = Pose2dWithCurvature(
            this.pose + other,
            curvature
    )
}

/**
 * @param curvature 1/m
 * @param dkds derivative of curvature
 */
data class Curvature(
    val curvature: Double,
    val dkds: Double
) : Interpolatable<Curvature> {
    override fun interpolate(endValue: Curvature, t: Double) =
            Curvature(
                    curvature.lerp(endValue.curvature, t),
                    dkds.lerp(endValue.dkds, t)
            )

    operator fun unaryMinus() = Curvature(
            -curvature,
            -dkds
    )
}
