package frc.team4069.saturn.lib.mathematics.control.coeffs

import frc.team4069.saturn.lib.mathematics.control.RealMatrix
import koma.util.validation.validate

data class StateSpaceObserverCoeffs(private val states: Int, private val outputs: Int,
                                    val K: RealMatrix) {
    init {
        validate {
            K("K") { states x outputs }
        }
    }
}