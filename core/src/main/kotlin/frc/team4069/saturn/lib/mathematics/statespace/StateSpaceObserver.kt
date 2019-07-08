package frc.team4069.saturn.lib.mathematics.statespace

import frc.team4069.saturn.lib.mathematics.statespace.coeffs.StateSpaceObserverCoeffs
import frc.team4069.keigen.*

/**
 * A state space observer for the given state space loop
 */
class StateSpaceObserver<States: Num, Inputs: Num, Outputs: Num>(coeffs: StateSpaceObserverCoeffs<States, Outputs>, val plant: StateSpacePlant<States, Inputs, Outputs>) {
    private val coefficients = mutableListOf(coeffs)
    private val index = 0

    /**
     * The estimate of the unknown state
     */
    var xHat: Vector<States> = zeros(plant.states)
        internal set

    /**
     * The kalman gain matrix
     */
    val K: Matrix<States, Outputs> get() = coefficients[index].K

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
    fun predict(newU: Vector<Inputs>) {
        xHat = plant.updateX(xHat, newU)
    }

    /**
     * The correct step of the kalman filter
     */
    fun correct(u: Vector<Inputs>, y: Vector<Outputs>) {
        xHat += K * (y - plant.C * xHat - plant.D * u)
    }
}
