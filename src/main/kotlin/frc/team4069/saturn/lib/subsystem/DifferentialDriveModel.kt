package frc.team4069.saturn.lib.subsystem

import frc.team4069.saturn.lib.mathematics.twodim.control.TrajectoryTracker
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.meter
import kotlin.math.sign

data class DifferentialDriveModel(
        val wheelBase: Length,
        val kV: Double,
        val kA: Double,
        val kS: Double
) {
    fun getDemand(output: TrajectoryTracker.TrajectoryTrackerOutput): DifferentialDriveDemand {
        val (leftVel, rightVel, leftAcc, rightAcc) = inverseKinematics(output)
        val leftArbFF = kV * leftVel + kA * leftAcc + kS * sign(leftVel)
        val rightArbFF = kV * rightVel + kA * rightAcc + kS * sign(rightVel)

        return DifferentialDriveDemand(leftVel.meter.velocity, rightVel.meter.velocity, leftArbFF, rightArbFF)
    }

    private fun inverseKinematics(output: TrajectoryTracker.TrajectoryTrackerOutput): WheelState {
        val leftVel = ((-wheelBase.value * output.angularVelocity.value + 2 * output.linearVelocity.value) / 2.0)
        val rightVel = ((wheelBase.value * output.angularVelocity.value + 2 * output.linearVelocity.value) / 2.0)

        val leftAcc = ((-wheelBase.value * output.angularAcceleration.value + 2 * output.linearVelocity.value) / 2.0)
        val rightAcc = ((wheelBase.value * output.angularAcceleration.value + 2 * output.linearAcceleration.value) / 2.0)

        return WheelState(leftVel, rightVel, leftAcc, rightAcc)
    }

    // Units are erased in this class because it's an intermediate to the actual output. All values are metric
    private data class WheelState(
            val leftVel: Double,
            val rightVel: Double,
            val leftAcc: Double,
            val rightAcc: Double
    )

    data class DifferentialDriveDemand(
            val leftSetpoint: LinearVelocity,
            val rightSetpoint: LinearVelocity,
            val leftArbFF: Double,
            val rightArbFF: Double
    )
}
