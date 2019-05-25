package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.keigen.*

data class StateSpaceControllerCoeffs<I: `100`, S: `100`>(val K: Matrix<I, S>,
                                                          val Kff: Matrix<I, S>,
                                                          val Umin: Vector<I>,
                                                          val Umax: Vector<I>)
