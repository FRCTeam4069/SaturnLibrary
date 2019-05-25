package frc.team4069.saturn.lib.mathematics.statespace

import frc.team4069.saturn.lib.mathematics.statespace.coeffs.StateSpaceControllerCoeffs
import koma.extensions.get
import koma.extensions.set
import koma.util.validation.validate
import koma.zeros
import frc.team4069.keigen.*

/**
 * A state space controller for the given plant
 */
class StateSpaceController<I: `100`, S: `100`, O: `100`>(coeffs: StateSpaceControllerCoeffs<I, S>, val plant: StateSpacePlant<I, S, O>) {
    private val index = 0
    private val coefficients = mutableListOf(coeffs)

    private val states = plant.states
    private val inputs = plant.inputs

    /**
     * The gain matrix
     */
    val K: Matrix<I, S> get() = coefficients[index].K

    /**
     * The feedforward gain matrix
     */
    val Kff: Matrix<I, S> get() = coefficients[index].Kff

    /**
     * The minimum value the control input can take
     */
    val Umin: Vector<I> get() = coefficients[index].Umin

    /**
     * The maximum value the control input can take
     */
    val Umax: Vector<I> get() = coefficients[index].Umax

    /**
     * The reference vector
     */
    var r: Vector<S> = zeros(states)
        private set

    /**
     * The input vector (Note that it is an input to the plant)
     */
    var u: Vector<I> = zeros(inputs)
        private set

    /**
     * Resets the reference and input for this controller
     */
    fun reset() {
        r = zeros(states)
        u = zeros(inputs)
    }

    /**
     * Updates the state of this controller with the given state `x` without updating the reference
     */
    fun update(x: Vector<S>) {
        u = K * (r - x) + Kff * (r - plant.A * r)
        capU()
    }

    /**
     * Updates the state of this controller with the given state `x`, along with the next reference for the controller
     */
    fun update(x: Vector<S>, nextR: Vector<S>) {
        u = K * (r - x) + Kff * (nextR - plant.A * r)
        r = nextR
        capU()
    }

    /**
     * Clamps the value of u to be between Umin and Umax
     */
    private fun capU() {
        for (i in 0 until inputs.i) {
            u[i] = u[i].coerceIn(Umin[i], Umax[i])
        }
    }
}
