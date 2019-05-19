package frc.team4069.saturn.lib.mathematics.statespace

import frc.team4069.saturn.lib.mathematics.statespace.coeffs.StateSpaceControllerCoeffs
import koma.extensions.get
import koma.extensions.set
import koma.matrix.Matrix
import koma.util.validation.validate
import koma.zeros

typealias RealMatrix = Matrix<Double> // Faster to type

/**
 * A state space controller for the given plant
 */
class StateSpaceController(coeffs: StateSpaceControllerCoeffs, val plant: StateSpacePlant) {
    private val index = 0
    private val coefficients = mutableListOf(coeffs)

    private val states = plant.states
    private val inputs = plant.inputs

    /**
     * The gain matrix
     */
    val K: RealMatrix get() = coefficients[index].K

    /**
     * The feedforward gain matrix
     */
    val Kff: RealMatrix get() = coefficients[index].Kff

    /**
     * The minimum value the control input can take
     */
    val Umin: RealMatrix get() = coefficients[index].Umin

    /**
     * The maximum value the control input can take
     */
    val Umax: RealMatrix get() = coefficients[index].Umax

    /**
     * The reference vector
     */
    var r = zeros(states, 1)
        private set

    /**
     * The input vector (Note that it is an input to the plant)
     */
    var u = zeros(inputs, 1)
        private set

    /**
     * Resets the reference and input for this controller
     */
    fun reset() {
        r = zeros(states, 1)
        u = zeros(inputs, 1)
    }

    /**
     * Updates the state of this controller with the given state `x` without updating the reference
     */
    fun update(x: RealMatrix) {
        u = K * (r - x) + Kff * (r - plant.A * r)
        capU()
    }

    /**
     * Updates the state of this controller with the given state `x`, along with the next reference for the controller
     */
    fun update(x: RealMatrix, nextR: RealMatrix) {
        validate {
            x("x") { states x 1 }
            nextR("r") { states x 1 }
        }

        u = K * (r - x) + Kff * (nextR - plant.A * r)
        r = nextR
        capU()
    }

    /**
     * Clamps the value of u to be between Umin and Umax
     */
    private fun capU() {
        for (i in 0 until inputs) {
            u[i, 0] = u[i, 0].coerceIn(Umin[i, 0], Umax[i, 0])
        }
    }
}
