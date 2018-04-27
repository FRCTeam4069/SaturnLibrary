package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.buttons.JoystickButton
import edu.wpi.first.wpilibj.command.Command

/**
 * Represents a button on a human interface device. Constructed from [Controller.button]
 */
class ControllerButton internal constructor(id: Int, joystick: GenericHID) {
    private val backing = JoystickButton(joystick, id)

    /**
     * Updates the command to be run when the button is pressed in the backing field.
     * Returns `this` for easy chaining
     */
    fun whenPressed(command: Command): ControllerButton {
        backing.whenPressed(command)

        return this
    }

    /**
     * Updates the command to be run when the button is released in the backing field.
     *
     * Returns `this` for easy chaining
     */
    fun whenReleased(command: Command): ControllerButton {
        backing.whenReleased(command)

        return this
    }

    /**
     * Updates the command to be run while the button is held in the backing field
     *
     * Returns `this` for easy chaining
     */
    fun whileHeld(command: Command): ControllerButton {
        backing.whileHeld(command)

        return this
    }

    /**
     * Returns the raw value of the button; whether it is pressed or not.
     */
    val value: Boolean get() = backing.get()
}

