package frc.team4069.saturn.lib.mathematics.control

import koma.util.validation.validate
import koma.zeros

class StateSpaceLoop(val plant: StateSpacePlant, val controller: StateSpaceController, val observer: StateSpaceObserver) {

    var nextR = zeros(plant.states, 1)

    val enabled get() = controller.enabled

    fun enable() {
        controller.enable()
    }

    fun disable() {
        controller.disable()
    }

    fun reset() {
        plant.reset()
        controller.reset()
        observer.reset()
        nextR = zeros(plant.states, 1)
    }

    var xHat
        get() = observer.xHat
        set(value) {
            observer.xHat = value
        }

    val error get() = controller.r - xHat

    val u get() = controller.u

    fun correct(y: RealMatrix) {
        validate {
            y("y") { plant.outputs x 1}
        }
        observer.correct(controller.u, y)
    }

    fun predict() {
        controller.update(nextR, observer.xHat)
        observer.predict(controller.u)
    }


}