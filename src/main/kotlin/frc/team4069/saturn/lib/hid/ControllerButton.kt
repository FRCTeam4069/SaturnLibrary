package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.builtins.InstantRunnableCommand
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext

/**
 * Represents a button on a human interface device. Constructed from [Controller.button]
 */
class ControllerButton internal constructor(val id: Int, val joystick: GenericHID) {

    private val listeners = mutableListOf<Job>()

    /**
     * Updates the command to be run when the button is pressed in the backing field.
     * Returns `this` for easy chaining
     */
    fun whenPressed(command: Command): ControllerButton {
//        backing.whenPressed(command)
//        Scheduler.addButtonScheduler(object : Runnable {
//            var pressed = false
//
//            override fun run() {
//                if (backing.get() && !pressed) {
//                    pressed = true
//                    Scheduler.add(command)
//                } else if (!backing.get() && pressed) {
//                    Scheduler.cancel(command)
//                    pressed = false
//                }
//            }
//        })

        return this
    }

    /**
     * Updates the command to be run when the button is released in the backing field.
     *
     * Returns `this` for easy chaining
     */
    fun whenReleased(command: Command): ControllerButton {
//        backing.whenReleased(command)
//        Scheduler.addButtonScheduler(object : Runnable {
//            var pressed = false
//
//            override fun run() {
//                if (backing.get()) {
//                    if (!pressed) {
//                        pressed = true
//                    } else {
//                        Scheduler.cancel(command)
//                    }
//                } else if (!backing.get() && pressed) {
//                    Scheduler.add(command)
//                    pressed = false
//                }
//            }
//        })

        return this
    }

    fun pressed(command: Command) {
        val job = launch(buttonListenerPool) {
            while(isActive) {
                if(joystick.getRawButtonPressed(id)) {
                    command.start()
                }
                delay(20)
            }
        }

        listeners.add(job)
    }

    fun pressed(block: suspend () -> Unit) {
        pressed(InstantRunnableCommand(block))
    }

    fun released(command: Command) {
        val job = launch(buttonListenerPool) {
            while(isActive) {
                if(joystick.getRawButtonReleased(id)) {
                    command.start()
                }
            }
        }

        listeners.add(job)
    }

    fun released(block: suspend () -> Unit) {
        released(InstantRunnableCommand(block))
    }

    fun clearListeners() {
        listeners.forEach {
            it.cancel()
        }
        listeners.clear()
    }

    /**
     * Returns the raw value of the button; whether it is pressed or not.
     */
    val value: Boolean get() = joystick.getRawButton(id)

    companion object {
        val buttonListenerPool = newFixedThreadPoolContext(2, "Controller Listener")
    }
}

