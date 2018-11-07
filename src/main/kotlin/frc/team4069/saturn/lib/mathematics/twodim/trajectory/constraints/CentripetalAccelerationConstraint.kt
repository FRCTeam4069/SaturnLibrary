/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

package frc.team4069.saturn.lib.mathematics.twodim.trajectory.constraints

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.acceleration
import frc.team4069.saturn.lib.mathematics.units.meter
import kotlin.math.absoluteValue

class CentripetalAccelerationConstraint(
    val mMaxCentripetalAccelRaw: Double
) : TimingConstraint<Pose2dWithCurvature> {

    val mMaxCentripetalAccel
        get() = mMaxCentripetalAccelRaw.meter.acceleration

    constructor(mMaxCentripetalAccel: Acceleration) :
            this(mMaxCentripetalAccel.value)

    override fun getMaxVelocity(state: Pose2dWithCurvature) =
            Math.sqrt((mMaxCentripetalAccelRaw / state.curvature.curvature).absoluteValue)

    override fun getMinMaxAcceleration(
        state: Pose2dWithCurvature,
        velocity: Double
    ) = TimingConstraint.MinMaxAcceleration.kNoLimits
}