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

package frc.team4069.saturn.lib.commands

import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.experimental.command.SendableSubsystemBase
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

/**
 *  Kotlin wrapper for [SendableSubsystemBase]
 */
abstract class SaturnSubsystem : SendableSubsystemBase() {
    open fun lateInit() {}
    open fun autoReset() {}
    open fun teleopReset() {}
    open fun setNeutral() {}
}
