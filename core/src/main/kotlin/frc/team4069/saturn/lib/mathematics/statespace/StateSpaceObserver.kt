/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package frc.team4069.saturn.lib.mathematics.statespace

import edu.wpi.first.wpiutil.math.Matrix
import edu.wpi.first.wpiutil.math.Num
import frc.team4069.saturn.lib.mathematics.statespace.coeffs.StateSpaceObserverCoeffs
import frc.team4069.saturn.lib.mathematics.matrix.*

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
