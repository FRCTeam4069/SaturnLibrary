package frc.team4069.saturn.lib.mathematics.twodim.trajectory

import edu.wpi.first.wpilibj.trajectory.Trajectory
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.Second
import frc.team4069.saturn.lib.mathematics.units.second

fun Trajectory.iterator() = TrajectoryIterator(this)

class TrajectoryIterator(private val trajectory: Trajectory) {
    private var timeElapsed = 0.second

    fun advance(t: SIUnit<Second>) {
        timeElapsed += t
    }

    val currentState: Trajectory.State
        get() = trajectory.sample(timeElapsed.value)

    val isDone: Boolean
        get() = timeElapsed.value >= trajectory.totalTimeSeconds
}