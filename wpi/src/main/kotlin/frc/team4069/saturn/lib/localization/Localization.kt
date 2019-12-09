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
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.Second
import frc.team4069.saturn.lib.util.Source

abstract class Localization(
        protected val robotHeading: Source<Rotation2d>
) : Source<Pose2d> {
    var robotPosition = Pose2d()
        private set

    // Interpolatable buffer for previous poses
    private val interpolatableLocalizationBuffer = TimeInterpolatableBuffer<Pose2d>()

    private var prevHeading = Rotation2d(0.0)
    private var headingOffset = Rotation2d(0.0)


    init {
        interpolatableLocalizationBuffer[0.0] = Pose2d()
    }

    @Synchronized
    fun reset(newPosition: Pose2d = Pose2d()) = resetInternal(newPosition)

    open fun resetInternal(newPosition: Pose2d) {
        robotPosition = newPosition
        val newHeading = robotHeading()
        prevHeading = newHeading
        headingOffset = -newHeading + newPosition.rotation
        interpolatableLocalizationBuffer.clear()
    }

    @Synchronized
    fun update() {
        val newHeading = robotHeading()

        val deltaHeading = newHeading - prevHeading
        val newPos = robotPosition + update(deltaHeading)
        robotPosition = Pose2d(
                newPos.translation,
                newHeading + headingOffset
        )

        prevHeading = newHeading

        interpolatableLocalizationBuffer[Timer.getFPGATimestamp()] = robotPosition
    }

    protected abstract fun update(deltaHeading: Rotation2d): Pose2d

    override fun invoke() = robotPosition
    operator fun get(t: SIUnit<Second>) = get(t.value)
    internal operator fun get(timestamp: Double) = interpolatableLocalizationBuffer[timestamp]
}