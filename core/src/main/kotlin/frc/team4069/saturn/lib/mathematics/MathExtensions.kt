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

package frc.team4069.saturn.lib.mathematics

import kotlin.math.PI
import kotlin.math.absoluteValue

fun Double.lerp(endValue: Double, t: Double) = this + (endValue - this) * t.coerceIn(0.0, 1.0)

fun <T : Comparable<T>> min(a: T, b: T) = if (a < b) a else b
fun <T : Comparable<T>> max(a: T, b: T) = if (a > b) a else b

infix fun Double.epsilonEquals(other: Double): Boolean {
    return (this - other).absoluteValue < kEpsilon
}

infix fun Double.cos(other: Double): Double {
    return this * Math.cos(other)
}

infix fun Double.sin(other: Double): Double {
    return this * Math.sin(other)
}

fun Double.boundRadians(): Double {
    var x = this
    while (x >= PI) x -= (2 * PI)
    while (x < -PI) x += (2 * PI)
    return x
}
