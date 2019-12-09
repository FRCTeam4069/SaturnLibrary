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

import frc.team4069.saturn.lib.mathematics.twodim.control.TrajectoryTrackerOutput
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.derived.AccelerationFeedforward
import frc.team4069.saturn.lib.mathematics.units.derived.VelocityFeedforward
import frc.team4069.saturn.lib.mathematics.units.derived.Volt

data class DifferentialDriveModel(
        val wheelBase: SIUnit<Meter>,
        val kV: SIUnit<VelocityFeedforward>,
        val kA: SIUnit<AccelerationFeedforward>,
        val kS: SIUnit<Volt>
) {
    fun getDemand(output: TrajectoryTrackerOutput): DifferentialDriveDemand {
        val (leftVel, rightVel, leftAcc, rightAcc) = inverseKinematics(output)
        val leftArbFF = kV * leftVel + kA * leftAcc + kS * sign(leftVel)
        val rightArbFF = kV * rightVel + kA * rightAcc + kS * sign(rightVel)

        return DifferentialDriveDemand(leftVel, rightVel, leftArbFF, rightArbFF)
    }

    private fun inverseKinematics(output: TrajectoryTrackerOutput): WheelState {
        val leftVel = ((-wheelBase * output.angularVelocity + 2 * output.linearVelocity) / 2.0)
        val rightVel = ((wheelBase * output.angularVelocity + 2 * output.linearVelocity) / 2.0)

        val leftAcc = ((-wheelBase * output.angularAcceleration + 2 * output.linearAcceleration) / 2.0)
        val rightAcc = ((wheelBase * output.angularAcceleration + 2 * output.linearAcceleration) / 2.0)

        return WheelState(leftVel, rightVel, leftAcc, rightAcc)
    }

    private data class WheelState(
            val leftVel: SIUnit<LinearVelocity>,
            val rightVel: SIUnit<LinearVelocity>,
            val leftAcc: SIUnit<LinearAcceleration>,
            val rightAcc: SIUnit<LinearAcceleration>
    )

    data class DifferentialDriveDemand(
            val leftSetpoint: SIUnit<LinearVelocity>,
            val rightSetpoint: SIUnit<LinearVelocity>,
            val leftArbFF: SIUnit<Volt>,
            val rightArbFF: SIUnit<Volt>
    )
}
