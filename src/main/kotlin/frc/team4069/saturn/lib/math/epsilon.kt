package frc.team4069.saturn.lib.math

import kotlin.math.abs

const val epsilon = 1E-9

infix fun Double.epsilonEquals(other: Double) = abs(this - other) < epsilon