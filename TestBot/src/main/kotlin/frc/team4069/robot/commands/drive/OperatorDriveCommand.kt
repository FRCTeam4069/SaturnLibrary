package frc.team4069.robot.commands.drive

import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.DriveBaseSubsystem

class OperatorDriveCommand : Command() {

    init {
        requires(DriveBaseSubsystem)
    }

    override fun initialize() {
        DriveBaseSubsystem.stop()
    }

    override fun execute() {

        val turning = OI.steeringAxis
        val speed = OI.driveSpeed

        DriveBaseSubsystem.drive(turning, speed)
    }

    override fun isFinished(): Boolean = false
}