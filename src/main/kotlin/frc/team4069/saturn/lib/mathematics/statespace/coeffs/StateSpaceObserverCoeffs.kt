package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.keigen.*

data class StateSpaceObserverCoeffs<S: `100`, O: `100`>(val K: Matrix<S, O>)
