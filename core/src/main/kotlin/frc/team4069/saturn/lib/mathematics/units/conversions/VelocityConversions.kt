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

package frc.team4069.saturn.lib.mathematics.units.conversions

import frc.team4069.saturn.lib.mathematics.units.Meter
import frc.team4069.saturn.lib.mathematics.units.Unitless
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.derived.Velocity
import frc.team4069.saturn.lib.mathematics.units.meter

typealias LinearVelocity = Velocity<Meter>
typealias AngularVelocity = Velocity<Unitless>

private val meterToFeet = 1.meter.feet
private val meterToInches = 1.meter.inch
private val secondsPerMinute = 1.minute.second

val SIUnit<LinearVelocity>.feetPerSecond get() = value * meterToFeet
val SIUnit<LinearVelocity>.feetPerMinute get() = feetPerSecond * secondsPerMinute
val SIUnit<LinearVelocity>.inchesPerSecond get() = value * meterToInches
