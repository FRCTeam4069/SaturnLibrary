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

package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import frc.team4069.saturn.lib.util.DeltaTime
import kotlin.math.pow
import kotlin.math.sqrt

class TrapezoidalProfile private constructor(
        val distance: SIUnit<Meter>,
        maxVelocity: SIUnit<LinearVelocity>,
        val maxAcceleration: SIUnit<LinearAcceleration>,
        val initialX: SIUnit<Meter>
) : IKinematicController {

    private var cruiseVelocity = maxVelocity

    private val tAccel: SIUnit<Second>
    private val xAccel: Double

    private val tCruise: SIUnit<Second>
    private val xCruise: Double

    private val sign: Double = sign(distance - initialX)

    init {
        val distance = abs(distance - initialX)
        tAccel = (maxVelocity / maxAcceleration).let {
            if (x(it.value, 0.0, 0.0, maxAcceleration.value) < distance.value / 2) {
                it.value.second // gross
            } else {
                cruiseVelocity = (maxAcceleration.value * sqrt((distance / maxAcceleration).value)).meter.velocity
                (cruiseVelocity / maxAcceleration).value.second
            }
        }

        xAccel = x(tAccel.value, 0.0, 0.0, maxAcceleration.value)

        // cruiseVelocity is potentially modified above, this stops a division by zero edge case
        tCruise = if(cruiseVelocity.value != 0.0) {
            ((distance.value - (2 * xAccel)) / cruiseVelocity.value).coerceAtLeast(0.0).second
        } else 0.second
        xCruise = x(tCruise.value, 0.0, cruiseVelocity.value, 0.0)
    }

    val t1 = tAccel
    val t2 = tAccel + tCruise
    val t3 = 2 * tAccel + tCruise

    // Loops
    private var elapsed = 0.second

    private val deltaTime = DeltaTime()

    override fun getVelocity(currentTime: SIUnit<Second>): PVAData {
        val dt = deltaTime.updateTime(currentTime)
        elapsed += dt

        return when {
            elapsed < t1 -> {
                val t = elapsed
                PVAData(
                        x(t.value, initialX.value, 0.0, sign * maxAcceleration.value),
                        v(t.value, 0.0, sign * maxAcceleration.value),
                        sign * maxAcceleration.value)
            }
            elapsed < t2 -> {
                val t = elapsed - t1
                PVAData(
                        x(t.value, sign * xAccel + initialX.value, sign * cruiseVelocity.value, 0.0),
                        v(t.value, sign * cruiseVelocity.value, 0.0),
                        0.0
                )
            }
            elapsed < t3 -> {
                val t = elapsed - t2
                PVAData(
                        x(t.value, sign * (xAccel + xCruise) + initialX.value, sign * cruiseVelocity.value, sign * -maxAcceleration.value),
                        v(t.value, sign * cruiseVelocity.value, sign * -maxAcceleration.value),
                        sign * -maxAcceleration.value
                )
            }
            else -> PVAData(distance.value, 0.0, 0.0)
        }
    }

    private fun x(t: Double, x0: Double, v0: Double, a: Double) = x0 + v0 * t + 0.5 * a * t.pow(2)
    private fun v(t: Double, v0: Double, a: Double) = v0 + a * t
}