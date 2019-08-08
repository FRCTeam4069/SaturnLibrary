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
import frc.team4069.saturn.lib.mathematics.lerp
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.TrajectoryIterator
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import frc.team4069.saturn.lib.types.VaryInterpolatable

class TimedTrajectory<S : VaryInterpolatable<S>>(
    points: List<TimedEntry<S>>
) : Trajectory<SIUnit<Second>, TimedEntry<S>>(points) {

    override fun sample(interpolant: SIUnit<Second>) = sample(interpolant.value)

    fun sample(interpolant: Double) = when {
        interpolant >= lastInterpolant.value -> TrajectorySamplePoint(getPoint(points.size - 1))
        interpolant <= firstInterpolant.value -> TrajectorySamplePoint(getPoint(0))
        else -> {
            val (index, entry) = points.asSequence()
                    .withIndex()
                    .first { (index, entry) -> index != 0 && entry.t.value >= interpolant }

            val prevEntry = points[index - 1]
            if (entry.t epsilonEquals prevEntry.t) TrajectorySamplePoint(entry, index, index)
            else TrajectorySamplePoint(
                    prevEntry.interpolate(
                            entry,
                            (interpolant - prevEntry.t.value) / (entry.t.value - prevEntry.t.value)
                    ),
                    index - 1,
                    index
            )
        }
    }

    override val firstState = points.first()
    override val lastState = points.last()

    override val firstInterpolant = firstState.t
    override val lastInterpolant = lastState.t

    override fun iterator() = TimedIterator(this)
}

data class TimedEntry<S : VaryInterpolatable<S>>(
    val state: S,
    val t: SIUnit<Second> = 0.second,
    val velocity: SIUnit<LinearVelocity> = 0.meter.velocity,
    val acceleration: SIUnit<LinearAcceleration> = 0.meter.acceleration
) : VaryInterpolatable<TimedEntry<S>> {

    override fun interpolate(endValue: TimedEntry<S>, interpolant: Double): TimedEntry<S> {
        val newT = t.value.lerp(endValue.t.value, interpolant)
        val deltaT = newT.second - t
        if (deltaT.value < 0.0) return endValue.interpolate(this, 1.0 - interpolant)

        val velocity = this.velocity
        val acceleration = this.acceleration

        val reversing = velocity.value < 0.0 || velocity.value epsilonEquals 0.0 && acceleration.value < 0.0

        val newV = velocity + acceleration * deltaT
        val newS = (if (reversing) -1.0 else 1.0) * (velocity * deltaT + 0.5 * acceleration * deltaT * deltaT)

        return TimedEntry(
                state.interpolate(endValue.state, (newS / state.distance(endValue.state)).value),
                newT.second,
                newV,
                acceleration
        )
    }

    override fun distance(other: TimedEntry<S>) = state.distance(other.state)
}

class TimedIterator<S : VaryInterpolatable<S>>(
    trajectory: TimedTrajectory<S>
) : TrajectoryIterator<SIUnit<Second>, TimedEntry<S>>(trajectory) {
    override fun addition(a: SIUnit<Second>, b: SIUnit<Second>) = a + b
}

fun TimedTrajectory<Pose2dWithCurvature>.mirror() = TimedTrajectory(
        points.map {
            TimedEntry(
                    it.state.mirror,
                    it.t,
                    it.velocity,
                    it.acceleration
            )
        }
)

fun TimedTrajectory<Pose2dWithCurvature>.transform(transform: Pose2d) = TimedTrajectory(
        points.map {
            TimedEntry(
                    it.state + transform,
                    it.t,
                    it.velocity,
                    it.acceleration
            )
        }
)