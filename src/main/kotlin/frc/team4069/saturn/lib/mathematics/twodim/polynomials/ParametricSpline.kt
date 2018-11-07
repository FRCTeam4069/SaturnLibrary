/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

package frc.team4069.saturn.lib.mathematics.twodim.polynomials

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Curvature
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Translation2d
import frc.team4069.saturn.lib.mathematics.units.Rotation2d

abstract class ParametricSpline {
    abstract fun getPoint(t: Double): Translation2d
    abstract fun getHeading(t: Double): Rotation2d
    abstract fun getCurvature(t: Double): Double
    abstract fun getDCurvature(t: Double): Double
    abstract fun getVelocity(t: Double): Double

    private fun getPose2d(t: Double): Pose2d {
        return Pose2d(getPoint(t), getHeading(t))
    }

    fun getPose2dWithCurvature(t: Double): Pose2dWithCurvature {
        return Pose2dWithCurvature(getPose2d(t), Curvature(getCurvature(t), getDCurvature(t) / getVelocity(t)))
    }
}
