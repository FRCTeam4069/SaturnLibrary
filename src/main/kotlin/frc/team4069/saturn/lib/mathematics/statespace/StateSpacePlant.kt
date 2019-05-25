package frc.team4069.saturn.lib.mathematics.statespace

import frc.team4069.saturn.lib.mathematics.statespace.coeffs.StateSpacePlantCoeffs
import frc.team4069.keigen.*

/**
 * A state space plant with the given system matrices
 */
class StateSpacePlant<I: `100`, S: `100`, O: `100`>(coeffs: StateSpacePlantCoeffs<I, S, O>) {

    private val index = 0
    private val coefficients = mutableListOf(coeffs)

    /**
     * The state to state system matrix
     */
    val A: Matrix<S, S> get() = coefficients[index].A

    /**
     * The input to state system matrix
     */
    val B: Matrix<S, I> get() = coefficients[index].B

    /**
     * The state to output system matrix
     */
    val C: Matrix<O, S> get() = coefficients[index].C

    /**
     * The input to output system matrix
     */
    val D: Matrix<O, I> get() = coefficients[index].D

    internal val states get() = coefficients[index].states
    internal val inputs get() = coefficients[index].inputs
    internal val outputs get() = coefficients[index].outputs

    /**
     * The internal state of this plant
     */
    var x: Vector<S> = zeros(states)
        private set

    /**
     * The outputs of this plant
     */
    var y: Vector<O> = zeros(outputs)
        private set

    /**
     * Updates x and y for the given input
     */
    fun update(u: Vector<I>) {
        x = updateX(x, u)
        y = updateY(u)
    }

    /**
     * Update step for x
     * x' = Ax + Bu
     */
//    internal fun updateX(x: Mat<Double, S, `1`>, u: Mat<Double, I, `1`>) = (A * x + B * u).toVec()

    internal fun updateX(x: Vector<S>, u: Vector<I>) = A * x + B * u

    /**
     * Update step for y
     * y = Cx + Du
     */
    internal fun updateY(u: Vector<I>): Vector<O> = C * x + D * u

    /**
     * Resets the state and output for this plant
     */
    fun reset() {
        x = zeros(states)
        y = zeros(outputs)
    }
}

