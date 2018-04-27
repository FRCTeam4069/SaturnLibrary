package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4069.robot.commands.elevator.OperatorControlElevatorCommand
import frc.team4069.saturn.lib.motor.SaturnSRX

object ElevatorSubsystem : Subsystem() {
    private val talon = SaturnSRX(16, reversed = true, slaveIds = *intArrayOf(15))

    private const val MAX_POSITION_TICKS = -29000

    init {
        //TODO: Maybe this'll fix motion magic.
        talon.apply {
            invertSensorPhase = false

            kP = 0.6
            kD = 0.1
            kF = 0.5

            motionAcceleration = 2500
            motionCruiseVelocity = 3000

            configReverseSoftLimitThreshold(MAX_POSITION_TICKS, 0)
            configReverseSoftLimitEnable(true, 0)
        }
    }

    override fun initDefaultCommand() {
        defaultCommand = OperatorControlElevatorCommand()
    }

    fun set(mode: ControlMode, value: Double) = talon.set(mode, value)

    val position: Int
        get() = talon.getSelectedSensorPosition(0)
}
