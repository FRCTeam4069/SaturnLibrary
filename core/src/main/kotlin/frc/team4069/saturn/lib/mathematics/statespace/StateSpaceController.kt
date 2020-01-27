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
import frc.team4069.saturn.lib.mathematics.matrix.*
import frc.team4069.saturn.lib.mathematics.statespace.coeffs.StateSpaceControllerCoeffs

/**
 * A state space controller for the given plant
 */
class StateSpaceController<States: Num, Inputs: Num, Outputs: Num>(coeffs: StateSpaceControllerCoeffs<States, Inputs>, val plant: StateSpacePlant<States, Inputs, Outputs>) {
    private val index = 0
    private val coefficients = mutableListOf(coeffs)

    private val states = plant.states
    private val inputs = plant.inputs

    /**
     * The gain matrix
     */
    val K: Matrix<Inputs, States> get() = coefficients[index].K

    /**
     * The feedforward gain matrix
     */
    val Kff: Matrix<Inputs, States> get() = coefficients[index].Kff

    /**
     * The minimum value the control input can take
     */
    val Umin: Vector<Inputs> get() = coefficients[index].Umin

    /**
     * The maximum value the control input can take
     */
    val Umax: Vector<Inputs> get() = coefficients[index].Umax

    /**
     * The reference vector
     */
    var r: Vector<States> = zeros(states)
        private set

    /**
     * The input vector (Note that it is an input to the plant)
     */
    var u: Vector<Inputs> = zeros(inputs)
        private set

    /**
     * Resets the reference and input for this controller
     */
    fun reset() {
        r = zeros(states)
        u = zeros(inputs)
    }

    /**
     * Updates the state of this controller with the given state `x` without updating the reference
     */
    fun update(x: Vector<States>) {
        u = K * (r - x) + Kff * (r - plant.A * r)
        capU()
    }

    /**
     * Updates the state of this controller with the given state `x`, along with the next reference for the controller
     */
    fun update(x: Vector<States>, nextR: Vector<States>) {
        u = K * (r - x) + Kff * (nextR - plant.A * r)
        r = nextR
        capU()
    }

    /**
     * Clamps the value of u to be between Umin and Umax
     */
    private fun capU() {
        for (i in 0 until inputs.num) {
            u[i] = u[i].coerceIn(Umin[i], Umax[i])
        }
    }
}
