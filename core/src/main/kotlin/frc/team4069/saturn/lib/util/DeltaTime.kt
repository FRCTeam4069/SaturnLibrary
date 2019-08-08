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

package frc.team4069.saturn.lib.util

import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.Second
import frc.team4069.saturn.lib.mathematics.units.second

class DeltaTime(startTime: SIUnit<Second> = (-1).second) {
    var deltaTime = 0.second
        private set
    var currentTime = startTime
        private set

    fun updateTime(newTime: SIUnit<Second>): SIUnit<Second> {
        deltaTime = if (currentTime.value < 0.0) {
            0.second
        } else {
            newTime - currentTime
        }
        currentTime = newTime
        return deltaTime
    }

    fun reset() {
        currentTime = (-1).second
    }
}