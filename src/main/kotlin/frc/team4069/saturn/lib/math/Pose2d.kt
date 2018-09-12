package frc.team4069.saturn.lib.math

data class Pose2d(var x: Double, var y: Double, var theta: Double) {
    operator fun plus(other: Pose2d) = Pose2d(x + other.x, y + other.y, theta + other.theta)
    operator fun minus(other: Pose2d) = Pose2d(x - other.x, y - other.y, theta - other.theta)
    operator fun times(scalar: Double) = Pose2d(scalar * x, scalar *  y, scalar * theta)
}
