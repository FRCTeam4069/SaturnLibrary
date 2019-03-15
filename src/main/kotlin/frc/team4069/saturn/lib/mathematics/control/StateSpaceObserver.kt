package frc.team4069.saturn.lib.mathematics.control

import frc.team4069.saturn.lib.mathematics.control.coeffs.StateSpaceObserverCoeffs
import koma.extensions.get
import koma.zeros

class StateSpaceObserver(coeffs: StateSpaceObserverCoeffs, val plant: StateSpacePlant) {
    private val coefficients = mutableListOf(coeffs)
    private val index = 0

    var xHat = zeros(plant.states, 1)

    val K: RealMatrix get() = coefficients[index].K

    fun K(i: Int, j: Int) = K[i, j]

    fun reset() {
        xHat = zeros(plant.states, 1)
    }

    fun predict(newU: RealMatrix) {
        xHat = plant.updateX(xHat, newU)
    }

    fun correct(u: RealMatrix, y: RealMatrix) {
        xHat += K * (y - plant.C * xHat - plant.D * u)
    }
}
