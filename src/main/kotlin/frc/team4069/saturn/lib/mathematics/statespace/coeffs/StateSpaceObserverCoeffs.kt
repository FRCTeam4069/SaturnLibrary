package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.keigen.*

data class StateSpaceObserverCoeffs<States: `100`, Outputs: `100`>(val K: Matrix<States, Outputs>)
