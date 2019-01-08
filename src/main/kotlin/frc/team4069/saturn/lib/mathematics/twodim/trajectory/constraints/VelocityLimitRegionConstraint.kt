/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

@file:Suppress("unused")

package frc.team4069.saturn.lib.mathematics.twodim.trajectory.constraints

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rectangle2d
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearVelocity

class VelocityLimitRegionConstraint(
    val region: Rectangle2d,
    val velocityLimitRaw: Double
) : TimingConstraint<Pose2dWithCurvature> {

    constructor(
        region: Rectangle2d,
        velocityLimit: LinearVelocity
    ) : this(
            region,
            velocityLimit.value
    )

    override fun getMaxVelocity(state: Pose2dWithCurvature) =
            if (state.pose.translation in region) velocityLimitRaw else Double.POSITIVE_INFINITY

    override fun getMinMaxAcceleration(
        state: Pose2dWithCurvature,
        velocity: Double
    ) = TimingConstraint.MinMaxAcceleration.kNoLimits
}