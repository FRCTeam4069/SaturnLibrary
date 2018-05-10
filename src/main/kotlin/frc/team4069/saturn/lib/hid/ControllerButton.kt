package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.buttons.JoystickButton
import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.Scheduler

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
//        backing.whenPressed(command)
        Scheduler.addButtonScheduler(object : Runnable {
            var pressed = false

            override fun run() {
                if (backing.get() && !pressed) {
                    pressed = true
                    Scheduler.add(command)
                } else if (!backing.get() && pressed) {
                    Scheduler.cancel(command)
                    pressed = false
                }
            }
        })

        return this
    }

    /**
     * Updates the command to be run when the button is released in the backing field.
     *
     * Returns `this` for easy chaining
     */
    fun whenReleased(command: Command): ControllerButton {
//        backing.whenReleased(command)
        Scheduler.addButtonScheduler(object : Runnable {
            var pressed = false

            override fun run() {
                if (backing.get()) {
                    if (!pressed) {
                        pressed = true
                    } else {
                        Scheduler.cancel(command)
                    }
                } else if (!backing.get() && pressed) {
                    Scheduler.add(command)
                    pressed = false
                }
            }
        })

        return this
    }

    /**
     * Returns the raw value of the button; whether it is pressed or not.
     */
    val value: Boolean get() = backing.get()
}

