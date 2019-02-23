package frc.team4069.saturn.lib.mathematics.control

import frc.team4069.saturn.lib.mathematics.control.coeffs.StateSpacePlantCoeffs
import koma.extensions.get
import koma.util.validation.validate
import koma.zeros

class StateSpacePlant(coeffs: StateSpacePlantCoeffs) {

    private val index = 0
    private val coefficients = mutableListOf(coeffs)

    val A: RealMatrix get() = coefficients[index].A
    val B: RealMatrix get() = coefficients[index].B
    val C: RealMatrix get() = coefficients[index].C
    val D: RealMatrix get() = coefficients[index].D

    internal val states get() = coefficients[index].states
    internal val inputs get() = coefficients[index].inputs
    internal val outputs get() = coefficients[index].outputs

    var x = RealMatrix(states, 1) { _, _ -> 0.0 }
    var y = RealMatrix(inputs, 1) { _, _ -> 0.0 }

    fun update(u: RealMatrix) {
        validate {
            u("u") { inputs x 1 }
        }

        x = updateX(u)
        y = updateY(u)
    }

    internal fun updateX(u: RealMatrix): RealMatrix = A * x + B * u

    internal fun updateY(u: RealMatrix): RealMatrix = C * x + D * u

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