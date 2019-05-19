package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.saturn.lib.mathematics.statespace.RealMatrix
import koma.util.validation.validate

data class StateSpaceControllerCoeffs(private val inputs: Int, private val states: Int,
                                      val K: RealMatrix,
                                      val Kff: RealMatrix,
                                      val Umin: RealMatrix,
                                      val Umax: RealMatrix) {
    init {
        // Validate dimensions of matrices. Can't be done at compile-time sadly
        validate {
            K("K") { inputs x states }
            Kff("Kff") { inputs x states }
            Umin("Umin") { inputs x 1 }
            Umax("Umax") { inputs x 1 }
        }
    }
}