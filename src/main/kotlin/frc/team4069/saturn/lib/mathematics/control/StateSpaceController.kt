package frc.team4069.saturn.lib.mathematics.control

import frc.team4069.saturn.lib.mathematics.control.coeffs.StateSpaceControllerCoeffs
import koma.extensions.get
import koma.extensions.set
import koma.matrix.Matrix
import koma.util.validation.validate
import koma.zeros

typealias RealMatrix = Matrix<Double> // Faster to type

class StateSpaceController(coeffs: StateSpaceControllerCoeffs, val plant: StateSpacePlant) {
    private val index = 0
    private val coefficients = mutableListOf(coeffs)
    internal var enabled = false

    internal val states = plant.states
    internal val inputs = plant.inputs
    internal val outputs = plant.outputs

    val K: RealMatrix get() = coefficients[index].K
    val Kff: RealMatrix get() = coefficients[index].Kff
    val Umin: RealMatrix get() = coefficients[index].Umin
    val Umax: RealMatrix get() = coefficients[index].Umax

    var r = zeros(states, 1)
    var u = zeros(inputs, 1)

    fun K(i: Int, j: Int) = K[i, j]
    fun Kff(i: Int, j: Int) = Kff[i, j]
    fun Umin(i: Int, j: Int) = Umin[i, j]
    fun Umax(i: Int, j: Int) = Umax[i, j]

    fun enable() {
        enabled = true
    }

    fun disable() {
        enabled = false
        u = zeros(inputs, 1)
    }

    fun reset() {
        r = zeros(states, 1)
        u = zeros(inputs, 1)
    }

    fun update(x: RealMatrix) {
        update(x, r)
    }

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
//            u = K * (r - x) + Kff * (nextR - plant.A * r)
            capU()
        }
    }

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
