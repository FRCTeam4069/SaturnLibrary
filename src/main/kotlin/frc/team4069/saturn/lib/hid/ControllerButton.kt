package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.buttons.JoystickButton
import edu.wpi.first.wpilibj.command.Command

class ControllerButton(id: Int, joystick: GenericHID) {
    private val backing = JoystickButton(joystick, id)

    fun whenPressed(command: Command): ControllerButton {
        backing.whenPressed(command)
        return this
    }

    fun whenReleased(command: Command): ControllerButton {
        backing.whenReleased(command)
        return this
    }

    fun whileHeld(command: Command): ControllerButton {
        backing.whileHeld(command)
        return this
    }
}
