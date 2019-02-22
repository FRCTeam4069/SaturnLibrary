package frc.team4069.saturn.lib.mathematics.control

import koma.matrix.Matrix

typealias RealMatrix = Matrix<Double> // Faster to type

data class StateSpaceGains(val A: RealMatrix, val B: RealMatrix, val C: RealMatrix, val D: RealMatrix,
                           val K: RealMatrix, val Umax: RealMatrix, val Umin: RealMatrix)


class StateSpaceController(gains: StateSpaceGains) {
    private val A = gains.A
    private val B = gains.B
    private val C = gains.C
    private val D = gains.D
    private val K = gains.K
    private val Umin = gains.Umin
    private val Umax = gains.Umax

    fun update(r: RealMatrix, x: RealMatrix): RealMatrix = K * (r - x)
}