package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.buttons.JoystickButton
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.InstantCommand

class HIDButton(id: XboxButton, joystick: GenericHID) {
    private val backing = JoystickButton(joystick, id.value)

    fun pressed(command: Command) {
        backing.whenPressed(command)
    }

    inline fun pressed(crossinline block: () -> Unit) {
        pressed(object : InstantCommand() {
            override fun initialize() {
                block()
            }
        })
    }

    fun released(command: Command) {
        backing.whenReleased(command)
    }

    inline fun released(crossinline block: () -> Unit) {
        released(object : InstantCommand() {
            override fun initialize() {
                block()
            }
        })
    }
}
