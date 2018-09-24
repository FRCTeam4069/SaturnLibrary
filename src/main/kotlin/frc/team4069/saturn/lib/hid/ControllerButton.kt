package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.saturn.lib.command.builtins.InstantRunnableCommand
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext

/**
 * Represents a button on a human interface device. Constructed from [Controller.button]
 */
class ControllerButton internal constructor(val id: Int, val joystick: GenericHID) {

    private val listeners = mutableListOf<Job>()

    suspend fun update() {

    }

    /**
     * Updates the command to be run when the button is changeOn in the backing field.
     * Returns `this` for easy chaining
     */
    fun whenPressed(command: Command): ControllerButton {
//        backing.whenPressed(command)
//        Scheduler.addButtonScheduler(object : Runnable {
//            var changeOn = false
//
//            override fun run() {
//                if (backing.get() && !changeOn) {
//                    changeOn = true
//                    Scheduler.add(command)
//                } else if (!backing.get() && changeOn) {
//                    Scheduler.cancel(command)
//                    changeOn = false
//                }
//            }
//        })
        pressed(command)

        return this
    }

    /**
     * Updates the command to be run when the button is changeOff in the backing field.
     *
     * Returns `this` for easy chaining
     */
    fun whenReleased(command: Command): ControllerButton {
//        backing.whenReleased(command)
//        Scheduler.addButtonScheduler(object : Runnable {
//            var changeOn = false
//
//            override fun run() {
//                if (backing.get()) {
//                    if (!changeOn) {
//                        changeOn = true
//                    } else {
//                        Scheduler.cancel(command)
//                    }
//                } else if (!backing.get() && changeOn) {
//                    Scheduler.add(command)
//                    changeOn = false
//                }
//            }
//        })

        return this
    }

    fun pressed(command: Command) {
        val job = launch(buttonListenerPool) {
            var enabled = false
            while(isActive) {
                if(joystick.getRawButton(id) && !enabled) {
                    println("Comamnd should start")
                    command.start()
                    enabled = true
                }else if(enabled) {
                    enabled = false
                }
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
     * Returns the raw value of the button; whether it is changeOn or not.
     */
    val value: Boolean get() = joystick.getRawButton(id)

    companion object {
        val buttonListenerPool = newFixedThreadPoolContext(2, "Controller Listener")
    }
}

