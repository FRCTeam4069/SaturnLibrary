package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4069.robot.commands.intake.OperatorControlIntakeCommand
import frc.team4069.saturn.lib.motor.SaturnSRX

object IntakeSubsystem : Subsystem() {
    private val talon = SaturnSRX(14, slaveIds = *intArrayOf(21))

    override fun initDefaultCommand() {
        defaultCommand = OperatorControlIntakeCommand()
    }

    fun set(spd: Double) = talon.set(ControlMode.PercentOutput, spd)

    fun stop() = talon.stop()
}