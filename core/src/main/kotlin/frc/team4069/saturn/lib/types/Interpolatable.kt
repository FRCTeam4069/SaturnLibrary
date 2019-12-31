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

package frc.team4069.saturn.lib.types

interface Interpolatable<T> {

    /**
     * The function calculates an interpolated value along the fraction
     * `t` between `0.0` and `1.0`. When `t` = 1.0,
     * `endVal` is returned.
     *
     * @param endValue
     * target value
     * @param t
     * fraction between `0.0` and `1.0`
     * @return interpolated value
     */
    fun interpolate(endValue: T, t: Double): T
}