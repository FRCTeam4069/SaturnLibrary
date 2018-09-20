package frc.team4069.saturn.lib.math.geometry

data class Translation2d(val x: Double, val y: Double) {

    constructor(other: Translation2d) : this(other.x, other.y)
    constructor(start: Translation2d, end: Translation2d) : this(end.x - start.x, end.y - start.y)
    constructor() : this(0.0, 0.0)

    val norm = Math.hypot(x, y)

    operator fun plus(other: Translation2d) = Translation2d(x + other.x, y + other.y)
    operator fun minus(other: Translation2d) = Translation2d(x - other.x, y - other.y)

    operator fun times(scalar: Double) = Translation2d(x * scalar, y * scalar)

    operator fun unaryMinus() = Translation2d(-x, -y)
}
