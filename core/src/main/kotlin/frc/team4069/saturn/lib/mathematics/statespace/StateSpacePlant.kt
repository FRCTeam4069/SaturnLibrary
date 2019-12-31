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

import frc.team4069.saturn.lib.mathematics.statespace.coeffs.StateSpacePlantCoeffs
import frc.team4069.keigen.*

/**
 * A state space plant with the given system matrices
 */
class StateSpacePlant<States: Num, Inputs: Num, Outputs: Num>(coeffs: StateSpacePlantCoeffs<States, Inputs, Outputs>) {

    private val index = 0
    private val coefficients = mutableListOf(coeffs)

    /**
     * The state to state system matrix
     */
    val A: Matrix<States, States> get() = coefficients[index].A

    /**
     * The input to state system matrix
     */
    val B: Matrix<States, Inputs> get() = coefficients[index].B

    /**
     * The state to output system matrix
     */
    val C: Matrix<Outputs, States> get() = coefficients[index].C

    /**
     * The input to output system matrix
     */
    val D: Matrix<Outputs, Inputs> get() = coefficients[index].D

    internal val states get() = coefficients[index].states
    internal val inputs get() = coefficients[index].inputs
    internal val outputs get() = coefficients[index].outputs

    /**
     * The internal state of this plant
     */
    var x: Vector<States> = zeros(states)
        private set

    /**
     * The outputs of this plant
     */
    var y: Vector<Outputs> = zeros(outputs)
        private set

    /**
     * Updates x and y for the given input
     */
    fun update(u: Vector<Inputs>) {
        x = updateX(x, u)
        y = updateY(u)
    }

    /**
     * Update step for x
     * x' = Ax + Bu
     */
    internal fun updateX(x: Vector<States>, u: Vector<Inputs>) = A * x + B * u

    /**
     * Update step for y
     * y = Cx + Du
     */
    internal fun updateY(u: Vector<Inputs>): Vector<Outputs> = C * x + D * u

    /**
     * Resets the state and output for this plant
     */
    fun reset() {
        x = zeros(states)
        y = zeros(outputs)
    }
}

