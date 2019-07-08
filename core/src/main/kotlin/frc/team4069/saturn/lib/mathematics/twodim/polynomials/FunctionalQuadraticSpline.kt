package frc.team4069.saturn.lib.mathematics.twodim.polynomials

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Translation2d
import frc.team4069.saturn.lib.mathematics.units.*

class FunctionalQuadraticSpline(
    private val p1: Translation2d,
    private val p2: Translation2d,
    private val p3: Translation2d
) {
    private val a get() = (p3.x * (p2.y - p1.y) + p2.x * (p1.y - p3.y) + p1.x * (p3.y - p2.y)).value
    private val b get() = (p3.x * p3.x * (p1.y - p2.y) + p2.x * p2.x * (p3.y - p1.y) + p1.x * p1.x * (p2.y - p3.y)).value

    val vertexXCoordinate get() = -b / (2 * a)
}