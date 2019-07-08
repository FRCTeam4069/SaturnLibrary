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