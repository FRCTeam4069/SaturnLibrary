package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.saturn.lib.mathematics.statespace.RealMatrix
import koma.util.validation.validate

data class StateSpacePlantCoeffs(val inputs: Int, val states: Int, val outputs: Int,
                                 val A: RealMatrix,
                                 val B: RealMatrix,
                                 val C: RealMatrix,
                                 val D: RealMatrix) {
    init {
        validate {
            A("A") { states x states }
            B("B") { states x inputs }
            C("C") { outputs x states }
            D("D") { outputs x inputs }
        }
    }
}