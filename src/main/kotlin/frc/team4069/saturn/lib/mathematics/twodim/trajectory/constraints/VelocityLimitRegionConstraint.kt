/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

@file:Suppress("unused")

package frc.team4069.saturn.lib.mathematics.twodim.trajectory.constraints

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rectangle2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Translation2d
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.meter

class VelocityLimitRegionConstraint(
    val region: Rectangle2d,
    val velocityLimitRaw: Double
) : TimingConstraint<Translation2d> {

    val velocityLimit
        get() = velocityLimitRaw.meter.velocity

    constructor(
        region: Rectangle2d,
        velocityLimit: LinearVelocity
    ) : this(
            region,
            velocityLimit.value
    )

    override fun getMaxVelocity(state: Translation2d) =
            if (state in region) velocityLimitRaw else Double.POSITIVE_INFINITY

    override fun getMinMaxAcceleration(
        state: Translation2d,
        velocity: Double
    ) = TimingConstraint.MinMaxAcceleration.kNoLimits
}