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

package frc.team4069.saturn.lib.localization

import edu.wpi.first.wpilibj.Timer
import frc.team4069.saturn.lib.mathematics.kinematics.SwerveDriveKinematics
import frc.team4069.saturn.lib.mathematics.kinematics.SwerveModuleState
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Twist2d
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.util.DeltaTime

class SwerveDriveLocalization(initialPose: Pose2d = Pose2d(), val kinematics: SwerveDriveKinematics) {
    var pose = initialPose
    var previousAngle = pose.rotation
    val deltaTime = DeltaTime()

    fun resetPosition(pose: Pose2d) {
        this.pose = pose
        previousAngle = pose.rotation
    }

    fun update(now: SIUnit<Second> = Timer.getFPGATimestamp().second, angle: Rotation2d,
               vararg moduleStates: SwerveModuleState): Pose2d {
        val dt = deltaTime.updateTime(now)

        val chassisState = kinematics.toChassisSpeeds(*moduleStates)
        val newPose = this.pose.exp(
                Twist2d(chassisState.vx * dt,
                        chassisState.vy * dt,
                        angle - previousAngle)
        )
        previousAngle = angle
        this.pose = Pose2d(newPose.translation, angle)
        return this.pose
    }
}
