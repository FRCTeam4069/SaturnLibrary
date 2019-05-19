package frc.team4069.saturn.lib.mathematics.statespace

import frc.team4069.saturn.lib.mathematics.statespace.coeffs.StateSpaceObserverCoeffs
import koma.extensions.get
import koma.zeros

/**
 * A state space observer for the given state space loop
 */
class StateSpaceObserver(coeffs: StateSpaceObserverCoeffs, val plant: StateSpacePlant) {
    private val coefficients = mutableListOf(coeffs)
    private val index = 0

    /**
     * The estimate of the unknown state
     */
    var xHat = zeros(plant.states, 1)
        internal set

    /**
     * The kalman gain matrix
     */
    val K: RealMatrix get() = coefficients[index].K

    fun K(i: Int, j: Int) = K[i, j]

    /**
     * Resets this estimate to 0
     */
    fun reset() {
        xHat = zeros(plant.states, 1)
    }

    /**
     * The predict step of the kalman filter
     */
    fun predict(newU: RealMatrix) {
        xHat = plant.updateX(xHat, newU)
    }

    /**
     * The correct step of the kalman filter
     */
    fun correct(u: RealMatrix, y: RealMatrix) {
        xHat += K * (y - plant.C * xHat - plant.D * u)
    }
}
