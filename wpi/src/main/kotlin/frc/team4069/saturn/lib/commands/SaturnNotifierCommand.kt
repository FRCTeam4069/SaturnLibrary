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

import edu.wpi.first.wpilibj2.command.Subsystem
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.Second
import frc.team4069.saturn.lib.util.launchFrequency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job

class SaturnNotifierCommand(private val period: SIUnit<Second>,
                            vararg subsystems: Subsystem,
                            private val block: suspend CoroutineScope.() -> Unit) : SaturnCommand(*subsystems) {
    private lateinit var job: Job

    override fun initialize() {
        job = GlobalScope.launchFrequency(period.invert(), block = block)
    }

    override fun end(interrupted: Boolean) {
        job.cancel()
    }
}
