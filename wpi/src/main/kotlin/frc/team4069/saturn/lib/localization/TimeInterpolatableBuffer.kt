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
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.Second
import frc.team4069.saturn.lib.mathematics.units.conversions.second
import frc.team4069.saturn.lib.mathematics.units.second
import frc.team4069.saturn.lib.types.Interpolatable
import frc.team4069.saturn.lib.util.Source
import java.util.*

class TimeInterpolatableBuffer<T : Interpolatable<T>>(
        private val historySpan: SIUnit<Second> = 1.second,
        private val timeSource: Source<SIUnit<Second>> = { Timer.getFPGATimestamp().second }
) {

    private val bufferMap = TreeMap<SIUnit<Second>, T>()

    operator fun set(time: SIUnit<Second>, value: T): T? {
        cleanUp()
        return bufferMap.put(time, value)
    }

    private fun cleanUp() {
        val currentTime = timeSource()
        val iterator = bufferMap.iterator()
        iterator.forEach {
            if (currentTime - it.key >= historySpan) {
                iterator.remove()
            }
        }
    }

    fun clear() {
        bufferMap.clear()
    }

    operator fun get(time: SIUnit<Second>): T {
        bufferMap[time]?.let { return it }

        val topBound = bufferMap.ceilingEntry(time)
        val bottomBound = bufferMap.floorEntry(time)

        return when {
            topBound == null -> bottomBound.value
            bottomBound == null -> topBound.value
            else -> bottomBound.value.interpolate(
                topBound.value,
                (time - bottomBound.key) / (topBound.key - bottomBound.key)
            )
        }
    }

    fun indexOf(state: T): Int? = bufferMap.values.indexOf(state)
}