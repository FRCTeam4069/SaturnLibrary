package frc.team4069.saturn.lib.mathematics.statespace.coeffs

import frc.team4069.keigen.*

data class StateSpacePlantCoeffs<States: Num, Inputs: Num, Outputs: Num>(val states: Nat<States>, val inputs: Nat<Inputs>, val outputs: Nat<Outputs>,
                                                                  val A: Matrix<States, States>,
                                                                  val B: Matrix<States, Inputs>,
                                                                  val C: Matrix<Outputs, States>,
                                                                  val D: Matrix<Outputs, Inputs>)
