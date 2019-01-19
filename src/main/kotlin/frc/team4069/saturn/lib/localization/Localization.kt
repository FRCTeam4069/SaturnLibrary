package frc.team4069.saturn.lib.localization

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.units.Rotation2d
import frc.team4069.saturn.lib.mathematics.units.radian
import frc.team4069.saturn.lib.util.Source

abstract class Localization(
        protected val robotHeading: Source<Rotation2d>
) : Source<Pose2d> {
    var robotPosition = Pose2d()
        private set

    // Interpolatable buffer for previous poses

    private var prevHeading = 0.radian
    private var headingOffset = 0.radian

    @Synchronized
    fun reset(newPosition: Pose2d = Pose2d()) = resetInternal(newPosition)

    open fun resetInternal(newPosition: Pose2d) {
        robotPosition = newPosition
        val newHeading = robotHeading()
        prevHeading = newHeading
        headingOffset = -newHeading + newPosition.rotation
    }

    @Synchronized
    fun update() {
        val newHeading = robotHeading()

        val deltaHeading = newHeading - prevHeading
        val newPos = robotPosition + update(deltaHeading)
        robotPosition = Pose2d(
                newPos.translation,
                newHeading + headingOffset
        )

        prevHeading = newHeading
    }

    protected abstract fun update(deltaHeading: Rotation2d): Pose2d

    override fun invoke() = robotPosition
}