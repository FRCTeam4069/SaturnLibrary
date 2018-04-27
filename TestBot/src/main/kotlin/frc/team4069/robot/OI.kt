package frc.team4069.robot

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.robot.commands.intake.SetIntakeSpeedCommand
import frc.team4069.robot.commands.intake.StopIntakeCommand
import frc.team4069.saturn.lib.hid.ButtonType
import frc.team4069.saturn.lib.hid.Controller

object OI {
    private val driveJoystick = Controller(0)
    private val controlJoystick = Controller(1)

    init {
        // Slow outtake command
        controlJoystick.button(ButtonType.BUMPER_RIGHT)
                .whenPressed(SetIntakeSpeedCommand(-0.5))
                .whenReleased(StopIntakeCommand())

    }

    val steeringAxis: Double
        get() {
            val axis = driveJoystick.getX(GenericHID.Hand.kLeft)
            return if(Math.abs(axis) in 0.0..0.2) {
                0.0
            }else {
                axis
            }
        }

    val driveSpeed: Double
        get() {
            val forward = driveJoystick.getRawAxis(3)
            val backward = driveJoystick.getRawAxis(2)

            return forward - backward
        }

    val elevatorAxis: Double
        get() {
            val axis = controlJoystick.getY(GenericHID.Hand.kRight)
            return if(Math.abs(axis) in 0.0..0.2) {
                0.0
            }else {
                axis
            }
        }

    val intakeAxis: Double
        get() {
            val forward = controlJoystick.getRawAxis(3)
            val backward = controlJoystick.getRawAxis(2)

            return forward - backward
        }
}