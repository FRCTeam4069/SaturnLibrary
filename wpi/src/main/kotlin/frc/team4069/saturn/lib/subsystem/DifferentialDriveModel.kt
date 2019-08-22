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

package frc.team4069.saturn.lib.subsystem

import frc.team4069.saturn.lib.mathematics.twodim.control.TrajectoryTracker
import frc.team4069.saturn.lib.mathematics.twodim.control.TrajectoryTrackerOutput
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import kotlin.math.sign

data class DifferentialDriveModel(
        val wheelBase: SIUnit<Meter>,
        val kV: Double,
        val kA: Double,
        val kS: Double
) {
    fun getDemand(output: TrajectoryTrackerOutput): DifferentialDriveDemand {
        val (leftVel, rightVel, leftAcc, rightAcc) = inverseKinematics(output)
        val leftArbFF = kV * leftVel + kA * leftAcc + kS * sign(leftVel)
        val rightArbFF = kV * rightVel + kA * rightAcc + kS * sign(rightVel)

        return DifferentialDriveDemand(leftVel.meter.velocity, rightVel.meter.velocity, leftArbFF, rightArbFF)
    }

    private fun inverseKinematics(output: TrajectoryTrackerOutput): WheelState {
        val leftVel = ((-wheelBase.value * output.angularVelocity.value + 2 * output.linearVelocity.value) / 2.0)
        val rightVel = ((wheelBase.value * output.angularVelocity.value + 2 * output.linearVelocity.value) / 2.0)

        val leftAcc = ((-wheelBase.value * output.angularAcceleration.value + 2 * output.linearVelocity.value) / 2.0)
        val rightAcc = ((wheelBase.value * output.angularAcceleration.value + 2 * output.linearAcceleration.value) / 2.0)

        return WheelState(leftVel, rightVel, leftAcc, rightAcc)
    }

    // Units are erased in this class because it's an intermediate to the actual output. All values are metric
    private data class WheelState(
            val leftVel: Double,
            val rightVel: Double,
            val leftAcc: Double,
            val rightAcc: Double
    )

    data class DifferentialDriveDemand(
            val leftSetpoint: SIUnit<LinearVelocity>,
            val rightSetpoint: SIUnit<LinearVelocity>,
            val leftArbFF: Double,
            val rightArbFF: Double
    )
}
