package frc.team4069.robot.commands

import edu.wpi.first.wpilibj.command.CommandGroup
import frc.team4069.robot.commands.drive.OperatorDriveCommand
import frc.team4069.robot.commands.elevator.OperatorControlElevatorCommand
import frc.team4069.robot.commands.intake.OperatorControlIntakeCommand

class OperatorControlCommandGroup : CommandGroup() {
    init {
        addParallel(OperatorDriveCommand())
        addParallel(OperatorControlElevatorCommand())
        addParallel(OperatorControlIntakeCommand())
    }
}