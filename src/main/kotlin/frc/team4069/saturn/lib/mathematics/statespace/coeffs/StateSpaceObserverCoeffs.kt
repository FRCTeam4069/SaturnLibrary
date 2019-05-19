package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.saturn.lib.mathematics.statespace.RealMatrix
import koma.util.validation.validate

data class StateSpaceObserverCoeffs(val states: Int, val outputs: Int,
                                    val K: RealMatrix) {
    init {
        validate {
            K("K") { states x outputs }
        }
    }
}