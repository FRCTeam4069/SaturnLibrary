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

package frc.team4069.saturn.lib.mathematics.twodim.trajectory.types

import frc.team4069.saturn.lib.mathematics.twodim.trajectory.TrajectoryIterator
import frc.team4069.saturn.lib.types.VaryInterpolatable

abstract class Trajectory<U : Comparable<U>, S : VaryInterpolatable<S>>(
    val points: List<S>
) {
    fun getPoint(index: Int) = TrajectoryPoint(index, points[index])

    abstract fun sample(interpolant: U): TrajectorySamplePoint<S>

    abstract val firstInterpolant: U
    abstract val lastInterpolant: U

    abstract val firstState: S
    abstract val lastState: S

    abstract operator fun iterator(): TrajectoryIterator<U, S>
}

data class TrajectoryPoint<S>(
    val index: Int,
    val state: S
)

data class TrajectorySamplePoint<S>(
    val state: S,
    val indexFloor: Int,
    val indexCeil: Int
) {
    constructor(point: TrajectoryPoint<S>) : this(
            point.state,
            point.index,
            point.index
    )
}