/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

package frc.team4069.saturn.lib.mathematics.twodim.trajectory.constraints

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearAcceleration
import kotlin.math.absoluteValue
import kotlin.math.sqrt

class CentripetalAccelerationConstraint(
    val mMaxCentripetalAccel: SIUnit<LinearAcceleration>
) : TimingConstraint<Pose2dWithCurvature> {

    override fun getMaxVelocity(state: Pose2dWithCurvature) =
            sqrt((mMaxCentripetalAccel.value / state.curvature.curvature.value).absoluteValue)

    override fun getMinMaxAcceleration(
        state: Pose2dWithCurvature,
        velocity: Double
    ) = TimingConstraint.MinMaxAcceleration.kNoLimits
}