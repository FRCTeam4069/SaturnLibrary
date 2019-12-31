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

package frc.team4069.saturn.lib.sensors

import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.sensors.PigeonIMU
import edu.wpi.first.wpilibj.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.util.Source

class SaturnPigeon(parentTalon: TalonSRX) : PigeonIMU(parentTalon), Source<Rotation2d> {
    // Accessors for Yaw, Pitch, and Roll.
    // fusedHeading is equivalent to yaw
    private var pitchOffset = Double.NaN
    private var rollOffset = Double.NaN

    init {
        val p = pitch
        if (!(p epsilonEquals 0.0)) {
            pitchOffset = p
        }

        val r = roll
        if(!(r epsilonEquals 0.0)) {
            rollOffset = r
        }
    }

    val fusedHeading: Rotation2d
        get() = Rotation2d.fromDegrees(getFusedHeading())

    val pitch: Double
        get() {
            val ypr = DoubleArray(3)
            getYawPitchRoll(ypr)
            return if (pitchOffset.isFinite()) {
                ypr[1] - pitchOffset
            } else {
                ypr[1]
            }
        }

    val roll: Double
        get() {
            val ypr = DoubleArray(3)
            getYawPitchRoll(ypr)

            return if(rollOffset.isFinite()) {
                ypr[2] - rollOffset
            } else {
                ypr[2]
            }
        }

    val quaternion: Quaternion
        get() {
            val arr = DoubleArray(4)
            get6dQuaternion(arr)

            return Quaternion(arr[0], arr[1], arr[2], arr[3])
        }

    override fun invoke() = fusedHeading

    data class Quaternion(val w: Double, val x: Double, val y: Double, val z: Double)
}