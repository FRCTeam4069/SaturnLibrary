package frc.team4069.robot.commands.elevator

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.ElevatorSubsystem

class OperatorControlElevatorCommand : Command() {

    private var set = true

    init {
        requires(ElevatorSubsystem)
    }

    override fun execute() {
        val elevatorAxis = OI.elevatorAxis * 0.8

        if(elevatorAxis != 0.0) {
            ElevatorSubsystem.set(ControlMode.PercentOutput, elevatorAxis)
            set = false
        }else {
            if(!set) {
                ElevatorSubsystem.set(ControlMode.MotionMagic, ElevatorSubsystem.position.toDouble())
                set = true
            }
        }
    }

    override fun isFinished(): Boolean = false
}
