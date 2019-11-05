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

import frc.team4069.keigen.*
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.TrajectoryIterator
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedEntry
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import kotlin.math.absoluteValue
import kotlin.math.sqrt

/**
 * Linear Time Varying Unicycle Controller. Optimal replacement for RAMSETE
 * Gains are determined via LQR.
 *
 * (https://file.tavsys.net/control/controls-engineering-in-frc.pdf Theorem 8.7.2)
 *
 * kX, kY0, kY1, and kTheta are tuning gains from LQR
 * robotVelocity is supplied by user code to determine the measured robot linear velocity
 */
class LTVUnicycleTracker(val kX: Double,
                         val kY0: Double,
                         val kY1: Double,
                         val kTheta: Double,
                         val robotVelocity: () -> SIUnit<LinearVelocity>) : TrajectoryTracker() {

    override fun calculateState(iterator: TrajectoryIterator<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>, robotPose: Pose2d): TrajectoryTrackerVelocityOutput {
        val referenceState = iterator.currentState.state

        // Get desired and actual velocities
        val v = robotVelocity()
        val vd = referenceState.velocity
        val wd = vd * referenceState.state.curvature.curvature

        // Calculate gain matrix at current robot velocity
        val K = K(v)

        val error = referenceState.state.pose inFrameOfReferenceOf robotPose

        val errorVec = vec(`3`).fill(error.translation.x.value, error.translation.y.value, error.rotation.value)
        // u = K(r - x), feedforward term is desired trajectory velocities
        val u = K * errorVec + vec(`2`).fill(vd.value, wd.value)

        return TrajectoryTrackerVelocityOutput(u[0].meter.velocity, u[1].radian.velocity)
    }

    private fun K(v: SIUnit<LinearVelocity>): Matrix<`2`, `3`> {
        return mat(`2`, `3`).fill(kX, 0.0, 0.0,
                0.0, kY(v) * sign(v), kTheta * sqrt(v.value.absoluteValue))
    }

    private fun kY(v: SIUnit<LinearVelocity>): Double {
        val value = v.value
        return kY0 + (kY1 - kY0) * sqrt(value.absoluteValue)
    }
}
