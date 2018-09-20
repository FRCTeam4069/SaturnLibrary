package frc.team4069.saturn.lib.math.geometry

data class Pose2d(val translation: Translation2d, val rotation: Rotation2d) {
    val x = translation.x
    val y = translation.y
    val theta = rotation.radians

    constructor() : this(Translation2d(), Rotation2d())
    constructor(x: Double, y: Double, theta: Double) : this(Translation2d(x, y), Rotation2d(theta))

    fun errorBy(expected: Pose2d): Pose2d {
        return Pose2d(
                rotation.cos * (expected.x - x) + rotation.sin * (expected.y - y),
                rotation.cos * (expected.y - y) - rotation.sin * (expected.x - x),
                expected.theta - rotation.radians
        )
    }

    operator fun plus(other: Pose2d): Pose2d {
        return Pose2d(x + other.x, y + other.y, other.theta)
    }
}