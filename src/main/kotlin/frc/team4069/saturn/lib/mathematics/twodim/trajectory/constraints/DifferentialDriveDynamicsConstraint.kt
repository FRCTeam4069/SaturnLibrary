package frc.team4069.saturn.lib.mathematics.twodim.trajectory.constraints

import com.team254.lib.physics.DifferentialDrive
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Volt

class DifferentialDriveDynamicsConstraint(
    private val drive: DifferentialDrive,
    private val maxVoltage: Volt
) : TimingConstraint<Pose2dWithCurvature> {

    override fun getMaxVelocity(state: Pose2dWithCurvature): Double {
        return drive.getMaxAbsVelocity(state.curvature.curvature.value, maxVoltage.value)
    }

    override fun getMinMaxAcceleration(state: Pose2dWithCurvature, velocity: Double): TimingConstraint.MinMaxAcceleration {
        val minMax = drive.getMinMaxAcceleration(
                DifferentialDrive.ChassisState(velocity, velocity * state.curvature.curvature.value),
                state.curvature.curvature.value,
                maxVoltage.value
        )
        return TimingConstraint.MinMaxAcceleration(minMax.min, minMax.max)
    }
}