package frc.team4069.saturn.lib.mathematics.twodim.trajectory

import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.derived.Curvature

@Suppress("FunctionName")
fun TrajectoryConfig(
        maxVelocity: SIUnit<LinearVelocity>,
        maxAcceleration: SIUnit<LinearAcceleration>,
        constraints: List<TrajectoryConstraint>,
        startVelocity: SIUnit<LinearVelocity>,
        endVelocity: SIUnit<LinearVelocity>,
        reversed: Boolean
) = edu.wpi.first.wpilibj.trajectory.TrajectoryConfig(
        maxVelocity.value,
        maxAcceleration.value).apply {
    addConstraints(constraints)
    setStartVelocity(startVelocity.value)
    setEndVelocity(endVelocity.value)
    isReversed = reversed
}

val Trajectory.State.time: SIUnit<Second>
    get() = timeSeconds.second

val Trajectory.State.velocity: SIUnit<LinearVelocity>
    get() = velocityMetersPerSecond.meter.velocity

val Trajectory.State.curvature: SIUnit<Curvature>
    get() = SIUnit(curvatureRadPerMeter)

val Trajectory.State.acceleration: SIUnit<LinearAcceleration>
    get() = accelerationMetersPerSecondSq.meter.acceleration
