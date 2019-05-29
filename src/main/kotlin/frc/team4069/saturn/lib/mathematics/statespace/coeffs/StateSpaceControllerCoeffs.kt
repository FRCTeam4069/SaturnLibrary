package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.keigen.*

data class StateSpaceControllerCoeffs<States: Num, Inputs: Num>(val K: Matrix<Inputs, States>,
                                                          val Kff: Matrix<Inputs, States>,
                                                          val Umin: Vector<Inputs>,
                                                          val Umax: Vector<Inputs>)
