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

/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

@file:Suppress("unused")

package frc.team4069.saturn.lib.mathematics.twodim.trajectory

import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.Trajectory
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TrajectorySamplePoint
import frc.team4069.saturn.lib.types.VaryInterpolatable

abstract class TrajectoryIterator<U : Comparable<U>, S : VaryInterpolatable<S>>(
    val trajectory: Trajectory<U, S>
) {

    protected abstract fun addition(a: U, b: U): U

    private var progress = trajectory.firstInterpolant
    private var sample = trajectory.sample(progress)

    val isDone
        get() = progress >= trajectory.lastInterpolant

    val currentState
        get() = sample

    fun advance(amount: U): TrajectorySamplePoint<S> {
        progress = addition(progress, amount)
                .coerceIn(trajectory.firstInterpolant, trajectory.lastInterpolant)
        sample = trajectory.sample(progress)
        return sample
    }

    fun preview(amount: U): TrajectorySamplePoint<S> {
        val progress = addition(progress, amount)
                .coerceIn(trajectory.firstInterpolant, trajectory.lastInterpolant)
        return trajectory.sample(progress)
    }
}