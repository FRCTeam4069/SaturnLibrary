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