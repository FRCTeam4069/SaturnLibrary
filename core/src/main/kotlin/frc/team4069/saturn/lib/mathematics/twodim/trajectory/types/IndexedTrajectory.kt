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

class IndexedTrajectory<S : VaryInterpolatable<S>>(
    points: List<S>
) : Trajectory<Double, S>(points) {

    override fun sample(interpolant: Double) = when {
        points.isEmpty() -> throw IndexOutOfBoundsException("Trajectory is empty!")
        interpolant <= 0.0 -> TrajectorySamplePoint(getPoint(0))
        interpolant >= points.size - 1 -> TrajectorySamplePoint(getPoint(points.size - 1))
        else -> {
            val index = Math.floor(interpolant).toInt()
            val percent = interpolant - index
            when {
                percent <= Double.MIN_VALUE -> TrajectorySamplePoint(getPoint(index))
                percent >= 1.0 - Double.MIN_VALUE -> TrajectorySamplePoint(getPoint(index + 1))
                else -> TrajectorySamplePoint(
                        points[index].interpolate(points[index], percent),
                        index,
                        index + 1
                )
            }
        }
    }

    override val firstState = points.first()
    override val lastState = points.last()

    override val firstInterpolant = 0.0
    override val lastInterpolant = (points.size - 1.0).coerceAtLeast(0.0)

    override fun iterator() = IndexedIterator(this)
}

class IndexedIterator<S : VaryInterpolatable<S>>(
    trajectory: IndexedTrajectory<S>
) : TrajectoryIterator<Double, S>(trajectory) {
    override fun addition(a: Double, b: Double) = a + b
}