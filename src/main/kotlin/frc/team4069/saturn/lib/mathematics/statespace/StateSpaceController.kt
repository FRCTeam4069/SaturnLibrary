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
    internal var enabled = false

    internal val states = plant.states
    internal val inputs = plant.inputs
    internal val outputs = plant.outputs

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

    fun K(i: Int, j: Int) = K[i, j]
    fun Kff(i: Int, j: Int) = Kff[i, j]
    fun Umin(i: Int, j: Int) = Umin[i, j]
    fun Umax(i: Int, j: Int) = Umax[i, j]

    /**
     * Enables this controller, allowing the value of u to be updated
     */
    fun enable() {
        enabled = true
    }

    /**
     * Disables this controller, and resets the value of u to [0]
     */
    fun disable() {
        enabled = false
        u = zeros(inputs, 1)
    }

    /**
     * Resets the reference and input for this controller
     */
    fun reset() {
        r = zeros(states, 1)
        u = zeros(inputs, 1)
    }

    /**
     * Updates the state of this controller with the given state `x`, and
     * optionally the next reference `nextR`
     */
    fun update(x: RealMatrix, nextR: RealMatrix? = null) {
        validate {
            x("x") { states x 1 }
            if(nextR != null) {
                nextR("r") { states x 1 }
            }
        }

        if (enabled) {
            u = if (nextR == null) {
                K * (r - x)
            } else {
                r = nextR
                K * (r - x) + Kff * (nextR - plant.A * r)
            }
            capU()
        }
    }

    /**
     * Clamps the value of u to be between Umin and Umax
     */
    private fun capU() {
        for (i in 0 until inputs) {
            if (u[i, 0] > Umax[i, 0]) {
                u[i, 0] = Umax[i, 0]
            } else if (u[i, 0] < Umin[i, 0]) {
                u[i, 0] = Umin[i, 0]
            }
        }
    }
}
