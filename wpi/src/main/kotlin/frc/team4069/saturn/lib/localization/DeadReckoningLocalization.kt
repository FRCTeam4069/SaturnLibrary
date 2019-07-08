package frc.team4069.saturn.lib.localization

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Twist2d
import frc.team4069.saturn.lib.mathematics.units.Meter
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.meter
import frc.team4069.saturn.lib.util.Source

class DeadReckoningLocalization(robotHeading: Source<Rotation2d>,
                                val leftEncoder: Source<SIUnit<Meter>>,
                                val rightEncoder: Source<SIUnit<Meter>>
) : Localization(robotHeading) {
    private var prevLeft = 0.0
    private var prevRight = 0.0

    override fun resetInternal(newPosition: Pose2d) {
        super.resetInternal(newPosition)
        prevLeft = leftEncoder().value
        prevRight = rightEncoder().value
    }

    override fun update(deltaHeading: Rotation2d): Pose2d {
        val newLeft = leftEncoder().value
        val newRight = rightEncoder().value

        val dl = newLeft - prevLeft
        val dr = newRight - prevRight

        this.prevLeft = newLeft
        this.prevRight = newRight

        return forwardKinematics(dl, dr, deltaHeading).asPose
    }

    private fun forwardKinematics(dl: Double, dr: Double, rotationDelta: Rotation2d): Twist2d {
        val dx = (dl + dr) / 2.0
        return Twist2d(dx.meter, 0.meter, rotationDelta)
    }
}