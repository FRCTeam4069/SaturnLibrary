package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearVelocity

data class MotionProfileConstraints(
        val maxVel: LinearVelocity,
        val maxAccel: LinearAcceleration
)