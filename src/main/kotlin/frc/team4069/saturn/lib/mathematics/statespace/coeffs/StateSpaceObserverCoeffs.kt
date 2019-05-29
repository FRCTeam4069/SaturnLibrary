package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.keigen.*

data class StateSpaceObserverCoeffs<States: Num, Outputs: Num>(val K: Matrix<States, Outputs>)
