package frc.team4069.saturn.lib.localization

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Twist2d
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.util.Source

class DeadReckoningLocalization(robotHeading: Source<Rotation2d>,
                                val leftEncoder: Source<Length>,
                                val rightEncoder: Source<Length>
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
        return Twist2d(dx, 0.0, rotationDelta)
    }
}