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

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

fun Double.safeRangeTo(endInclusive: Double) = min(this, endInclusive)..max(this, endInclusive)

// Eagerly evaluated deadband
fun Double.deadband(threshold: Double) = if(this.absoluteValue < threshold) 0.0 else this
