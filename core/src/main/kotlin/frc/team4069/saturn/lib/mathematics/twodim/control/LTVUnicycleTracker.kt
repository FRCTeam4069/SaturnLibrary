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
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.Trajectory
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.AngularVelocity
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import frc.team4069.saturn.lib.util.DeltaTime
import kotlin.math.absoluteValue
import kotlin.math.sqrt

/**
 * Linear Time Varying Unicycle Controller. Optimal replacement for RAMSETE
 * Gains are determined via LQR.
 *
 * (https://file.tavsys.net/control/controls-engineering-in-frc.pdf Theorem 8.7.2)
 */
class LTVUnicycleTracker(val kX: Double,
                         val kY0: Double,
                         val kY1: Double,
                         val kTheta: Double) : TrajectoryTracker() {

    override fun calculateState(iterator: TrajectoryIterator<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>, robotPose: Pose2d): TrajectoryTrackerVelocityOutput {
        val referenceState = iterator.currentState.state

        val vd = referenceState.velocity
        val wd = vd * referenceState.state.curvature.curvature

        val Kff = mat(`1`, `2`).fill(vd.value, wd.value)
        val K = K(vd)

        val error = referenceState.state.pose inFrameOfReferenceOf robotPose

        val errorVec = vec(`3`).fill(error.translation.x.value, error.translation.y.value, error.rotation.value)
        val u = K * errorVec //+ Kff * () (Feedforward later)

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