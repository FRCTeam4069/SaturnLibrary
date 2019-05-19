package frc.team4069.saturn.lib.mathematics.statespace

import koma.util.validation.validate
import koma.zeros

/**
 * A full state space loop encapsulating a plant, controller, and observer
 */
class StateSpaceLoop(val plant: StateSpacePlant, val controller: StateSpaceController, val observer: StateSpaceObserver) {

    /**
     * The next reference to be fed to the controller
     */
    var nextR = zeros(plant.states, 1)

    /**
     * Whether the controller for this loop is enabled
     */
    val enabled get() = controller.enabled

    /**
     * Enables the controller for this loop
     */
    fun enable() {
        controller.enable()
    }

    /**
     * Disables the controller for this loop
     */
    fun disable() {
        controller.disable()
    }

    /**
     * Resets all the internals of this loop, and zeroes the next reference
     */
    fun reset() {
        plant.reset()
        controller.reset()
        observer.reset()
        nextR = zeros(plant.states, 1)
    }

    /**
     * The estimated state from the kalman filter
     */
    var xHat
        get() = observer.xHat
        set(value) {
            validate {
                value("xHat") { plant.states x 1 }
            }
            observer.xHat = value
        }

    /**
     * The error of the current system, measured from the current reference vector for the controller
     */
    val error get() = controller.r - xHat

    /**
     * The current control input from the controller
     */
    val u get() = controller.u

    /**
     * Runs the correct step for the observer
     */
    fun correct(y: RealMatrix) {
        validate {
            y("y") { plant.outputs x 1}
        }
        observer.correct(controller.u, y)
    }

    /**
     * Updates the controller, and runs the predict step for the observer
     */
    fun predict() {
        controller.update(observer.xHat, nextR)
        observer.predict(controller.u)
    }
}