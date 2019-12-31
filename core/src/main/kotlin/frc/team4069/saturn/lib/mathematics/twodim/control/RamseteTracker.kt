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

package frc.team4069.saturn.lib.mathematics.twodim.control

import edu.wpi.first.wpilibj.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.TrajectoryIterator
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.curvature
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.velocity
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.AngularVelocity
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import kotlin.math.sin
import kotlin.math.sqrt

class RamseteTracker(
        private val kBeta: Double,
        private val kZeta: Double
) : TrajectoryTracker() {

    /**
     * Calculate desired chassis velocity using Ramsete.
     */
    override fun calculateState(
            iterator: TrajectoryIterator,
            robotPose: Pose2d
    ): TrajectoryTrackerVelocityOutput {
        val referenceState = iterator.currentState

        // Calculate goal in robot's coordinates
        val error = referenceState.poseMeters.relativeTo(robotPose)

        // Get reference linear and angular velocities
        val vd = referenceState.velocity
        val wd = vd * referenceState.curvature

        // Compute gain
        val k1 = 2 * kZeta * sqrt(wd.pow2().value + kBeta * vd.pow2().value)

        // Get angular error in bounded radians
        val angleError = error.rotation.radians

        return TrajectoryTrackerVelocityOutput(
                linearVelocity = (vd.value * error.rotation.cos + k1 * error.translation.x).meter.velocity,
                angularVelocity = (wd.value + (kBeta * sinc(angleError)) * (vd.value * error.translation.y) + k1 * angleError).radian.velocity
        )
    }

    companion object {
        private fun sinc(theta: Double): Double =
                if (theta epsilonEquals 0.0) {
                    1.0
                } else sin(theta) / theta
    }

}

operator fun <T: Key, U: Key> SIUnit<Mult<Fraction<T, U>, Fraction<T, U>>>.times(other: SIUnit<Fraction<Unitless, T>>): SIUnit<Fraction<T, Mult<U, U>>>
    = SIUnit(value * other.value)
