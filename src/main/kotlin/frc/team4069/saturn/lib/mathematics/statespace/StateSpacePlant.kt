package frc.team4069.saturn.lib.mathematics.statespace

import frc.team4069.saturn.lib.mathematics.statespace.coeffs.StateSpacePlantCoeffs
import koma.extensions.get
import koma.util.validation.validate
import koma.zeros

/**
 * A state space plant with the given system matrices
 */
class StateSpacePlant(coeffs: StateSpacePlantCoeffs) {

    private val index = 0
    private val coefficients = mutableListOf(coeffs)

    /**
     * The state to state system matrix
     */
    val A: RealMatrix get() = coefficients[index].A

    /**
     * The input to state system matrix
     */
    val B: RealMatrix get() = coefficients[index].B

    /**
     * The state to output system matrix
     */
    val C: RealMatrix get() = coefficients[index].C

    /**
     * The input to output system matrix
     */
    val D: RealMatrix get() = coefficients[index].D

    internal val states get() = coefficients[index].states
    internal val inputs get() = coefficients[index].inputs
    internal val outputs get() = coefficients[index].outputs

    /**
     * The internal state of this plant
     */
    var x = zeros(states, 1)
        private set

    /**
     * The outputs of this plant
     */
    var y = zeros(inputs, 1)
        private set

    /**
     * Updates x and y for the given input
     */
    fun update(u: RealMatrix) {
        validate {
            u("u") { inputs x 1 }
        }

        x = updateX(x, u)
        y = updateY(u)
    }

    /**
     * Update step for x
     * x' = Ax + Bu
     */
    internal fun updateX(x: RealMatrix, u: RealMatrix): RealMatrix = A * x + B * u

    /**
     * Update step for y
     * y = Cx + Du
     */
    internal fun updateY(u: RealMatrix): RealMatrix = C * x + D * u

    /**
     * Resets the state and output for this plant
     */
    fun reset() {
        x = zeros(states, 1)
        y = zeros(inputs, 1)
    }

    /**
     * Functions to get elements from the various system matrices
     */
    fun A(i: Int, j: Int) = A[i, j]

    fun B(i: Int, j: Int) = B[i, j]
    fun C(i: Int, j: Int) = C[i, j]
    fun D(i: Int, j: Int) = D[i, j]
}