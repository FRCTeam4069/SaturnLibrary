package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.keigen.*
import koma.util.validation.validate

data class StateSpacePlantCoeffs<I: `100`, S : `100`, O : `100`>(val states: Nat<S>, val inputs: Nat<I>, val outputs: Nat<O>,
                                                                  val A: Matrix<S, S>,
                                                                  val B: Matrix<S, I>,
                                                                  val C: Matrix<O, S>,
                                                                  val D: Matrix<O, I>)
