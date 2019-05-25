package frc.team4069.saturn.lib.mathematics.statespace

import frc.team4069.saturn.lib.mathematics.statespace.coeffs.StateSpaceObserverCoeffs
import koma.extensions.get
import koma.zeros
import frc.team4069.keigen.*

/**
 * A state space observer for the given state space loop
 */
class StateSpaceObserver<I: `100`, S: `100`, O: `100`>(coeffs: StateSpaceObserverCoeffs<S, O>, val plant: StateSpacePlant<I, S, O>) {
    private val coefficients = mutableListOf(coeffs)
    private val index = 0

    /**
     * The estimate of the unknown state
     */
    var xHat: Vector<S> = zeros(plant.states)
        internal set

    /**
     * The kalman gain matrix
     */
    val K: Matrix<S, O> get() = coefficients[index].K

    fun K(i: Int, j: Int) = K[i, j]

    /**
     * Resets this estimate to 0
     */
    fun reset() {
        xHat = zeros(plant.states)
    }

    /**
     * The predict step of the kalman filter
     */
    fun predict(newU: Vector<I>) {
        xHat = plant.updateX(xHat, newU)
    }

    /**
     * The correct step of the kalman filter
     */
    fun correct(u: Vector<I>, y: Vector<O>) {
        xHat += K * (y - plant.C * xHat - plant.D * u)
    }
}
