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

package frc.team4069.saturn.lib.mathematics.twodim.polynomials

import frc.team4069.saturn.lib.mathematics.twodim.geometry.*
import frc.team4069.saturn.lib.mathematics.units.curvature

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
        return Pose2dWithCurvature(getPose2d(t), Pose2dCurvature(getCurvature(t).curvature, getDCurvature(t) / getVelocity(t)))
    }
}
