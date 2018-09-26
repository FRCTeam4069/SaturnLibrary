package frc.team4069.saturn.lib.math

/**
 * Wrapper class representing the current position of the robot as recorded by encoders and gyro
 * [x] and [y] are cartesian coordinates, where y+ is left, y- is right, x+ is forward, and x- is back
 *
 * [theta] is the angle of the robot, in radians, where positive integers are angled towards the left, and negative are towards the right
 */
data class Pose2d(var x: Double, var y: Double, var theta: Double)
