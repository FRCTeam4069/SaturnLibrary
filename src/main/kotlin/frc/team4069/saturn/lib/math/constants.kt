package frc.team4069.saturn.lib.math

import kotlin.math.PI
import kotlin.math.abs

const val epsilon = 1E-9
const val TAU = 2 * PI

infix fun Double.epsilonEquals(other: Double) = abs(this - other) < epsilon