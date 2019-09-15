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

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Twist2d
import frc.team4069.saturn.lib.mathematics.units.Meter
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.meter
import frc.team4069.saturn.lib.util.Source

class DifferentialDriveLocalization(robotHeading: Source<Rotation2d>,
                                    val leftEncoder: Source<SIUnit<Meter>>,
                                    val rightEncoder: Source<SIUnit<Meter>>
) : Localization(robotHeading) {
    private var prevLeft = 0.0
    private var prevRight = 0.0

    override fun resetInternal(newPosition: Pose2d) {
        super.resetInternal(newPosition)
        prevLeft = leftEncoder().value
        prevRight = rightEncoder().value
    }

    override fun update(deltaHeading: Rotation2d): Pose2d {
        val newLeft = leftEncoder().value
        val newRight = rightEncoder().value

        val dl = newLeft - prevLeft
        val dr = newRight - prevRight

        this.prevLeft = newLeft
        this.prevRight = newRight

        return forwardKinematics(dl, dr, deltaHeading).asPose
    }

    private fun forwardKinematics(dl: Double, dr: Double, rotationDelta: Rotation2d): Twist2d {
        val dx = (dl + dr) / 2.0
        return Twist2d(dx.meter, 0.meter, rotationDelta)
    }
}