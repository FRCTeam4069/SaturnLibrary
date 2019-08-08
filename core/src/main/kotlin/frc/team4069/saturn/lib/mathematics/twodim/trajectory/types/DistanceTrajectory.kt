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

import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.TrajectoryIterator
import frc.team4069.saturn.lib.mathematics.units.Meter
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.meter
import frc.team4069.saturn.lib.types.VaryInterpolatable

class DistanceTrajectory<S : VaryInterpolatable<S>>(
    points: List<S>
) : Trajectory<SIUnit<Meter>, S>(points) {

    private val distances: List<Double>

    init {
        val tempDistances = mutableListOf<Double>()
        tempDistances += 0.0
        points.zipWithNext { c, n -> tempDistances += tempDistances.last() + c.distance(n) }
        distances = tempDistances
    }

    override fun sample(interpolant: SIUnit<Meter>) = sample(interpolant.value)

    fun sample(interpolant: Double) = when {
        interpolant >= lastInterpolant.value -> TrajectorySamplePoint(getPoint(points.size - 1))
        interpolant <= 0.0 -> TrajectorySamplePoint(getPoint(0))
        else -> {
            val (index, entry) = points.asSequence()
                    .withIndex()
                    .first { (index, _) -> index != 0 && distances[index] >= interpolant }

            val prevEntry = points[index - 1]
            if (distances[index] epsilonEquals distances[index - 1]) TrajectorySamplePoint(
                    entry,
                    index,
                    index
            ) else TrajectorySamplePoint(
                    prevEntry.interpolate(
                            entry,
                            (interpolant - distances[index - 1]) / (distances[index] - distances[index - 1])
                    ),
                    index - 1,
                    index
            )
        }
    }

    override val firstState = points.first()
    override val lastState = points.last()

    override val firstInterpolant = 0.meter
    override val lastInterpolant = distances.last().meter

    override fun iterator() = DistanceIterator(this)
}

class DistanceIterator<S : VaryInterpolatable<S>>(
    trajectory: DistanceTrajectory<S>
) : TrajectoryIterator<SIUnit<Meter>, S>(trajectory) {
    override fun addition(a: SIUnit<Meter>, b: SIUnit<Meter>) = a + b
}