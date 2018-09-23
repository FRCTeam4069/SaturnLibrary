package frc.team4069.saturn.lib.math.geometry

data class Pose2d(val translation: Translation2d, val rotation: Rotation2d) {
    constructor() : this(Translation2d(), Rotation2d())
    constructor(x: Double, y: Double, theta: Double) : this(Translation2d(x, y), Rotation2d(theta))
}