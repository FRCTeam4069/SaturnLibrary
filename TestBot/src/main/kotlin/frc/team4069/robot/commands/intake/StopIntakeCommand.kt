package frc.team4069.robot.commands.intake

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team4069.robot.subsystems.IntakeSubsystem

class StopIntakeCommand : InstantCommand() {
    override fun initialize() {
        IntakeSubsystem.stop()
    }
}