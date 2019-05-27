package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.keigen.*

data class StateSpaceObserverCoeffs<States: `50`, Outputs: `50`>(val K: Matrix<States, Outputs>)
