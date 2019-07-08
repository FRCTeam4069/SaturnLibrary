package frc.team4069.saturn.lib.mathematics.twodim.polynomials

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Translation2d
import frc.team4069.saturn.lib.mathematics.units.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
class FunctionalLinearSpline(val p1: Translation2d, val p2: Translation2d) {
    val m get() = (p2.y - p1.y) / (p2.x - p1.x)
    val b get() = (p1.y - (m * p1.x)).value

    val zero get() = -b / m
}