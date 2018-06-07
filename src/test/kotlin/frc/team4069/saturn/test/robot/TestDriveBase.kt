package frc.team4069.saturn.test.robot

import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.Subsystem

object TestDriveBase : Subsystem() {
    override val defaultCommand: Command? = null

    val leftDrive = StubSRX()
    val rightDrive = StubSRX()

    fun drive(turn: Double, speed: Double) {
        val speeds = cheesyDrive(turn, speed)

        leftDrive.speed = speeds.left
        rightDrive.speed = speeds.right
    }

    private fun cheesyDrive(turn: Double, speed: Double, scale: Double = 1.0): WheelSpeeds {
        if (speed == 0.0) {
            return if (turn == 0.0) {
                WheelSpeeds(0.0, 0.0)
            } else {
                WheelSpeeds(turn * scale, -turn * scale)
            }
        }

        val sign = Math.signum(speed)
        val wheelSpeedDifference = speedPolynomial(Math.abs(speed)) * turn * sign
        return WheelSpeeds(
                speed + wheelSpeedDifference,
                speed - wheelSpeedDifference
        )
    }

    private fun speedPolynomial(speed: Double) = Math.sqrt(speed) * 2

    data class WheelSpeeds(val left: Double, val right: Double)

    override fun toString() = "TestDriveBase(leftDrive=$leftDrive, rightDrive=$rightDrive)"

}