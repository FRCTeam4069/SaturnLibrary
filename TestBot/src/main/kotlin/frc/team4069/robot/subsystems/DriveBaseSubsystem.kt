package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4069.robot.commands.drive.OperatorDriveCommand
import frc.team4069.saturn.lib.motor.SaturnEncoder
import frc.team4069.saturn.lib.motor.SaturnSRX
import frc.team4069.saturn.lib.util.LowPassFilter

object DriveBaseSubsystem : Subsystem() {
    private val leftDrive = SaturnSRX(12, filter = LowPassFilter(50), slaveIds = *intArrayOf(11, 13))
    private val leftEncoder = SaturnEncoder(256, 0, 1)

    private val rightDrive = SaturnSRX(19, filter = LowPassFilter(50), slaveIds = *intArrayOf(18, 20))
    private val rightEncoder = SaturnEncoder(256, 8, 9)

    private const val METRES_PER_ROTATION = 0.61

    init {
        leftEncoder.apply {
            setMaxPeriod(0.1)
            setMinRate(10.0)
            distancePerPulse = 5.0
            setReverseDirection(true)
            samplesToAverage = 7
        }

        rightEncoder.apply {
            setMaxPeriod(0.1)
            setMinRate(10.0)
            distancePerPulse = 5.0
            setReverseDirection(true)
            samplesToAverage = 7
        }
    }

    override fun initDefaultCommand() {
        defaultCommand = OperatorDriveCommand()
    }

    fun stop() {
        leftDrive.stop()
        rightDrive.stop()
    }

    fun reset() {
        stop()
        leftEncoder.reset()
        rightEncoder.reset()
    }

    fun drive(turn: Double, speed: Double) {
        // Scale factor super low so that I don't hurt anyone with this experiment
        val speeds = cheesyDrive(turn, speed, scale = 0.6)
//        leftDrive.speed = speeds.left
//        rightDrive.speed = speeds.right
        leftDrive.set(ControlMode.PercentOutput, speeds.left)
        rightDrive.set(ControlMode.PercentOutput, speeds.right)
    }

    private fun cheesyDrive(turn: Double, speed: Double, scale: Double = 1.0): WheelSpeeds {
        if(speed == 0.0) {
            return WheelSpeeds(turn * scale, -turn * scale)
        }

        val sign = Math.signum(speed)
        val wheelSpeedDifference = speedPolynomial(Math.abs(speed)) * turn * sign
        return WheelSpeeds(
                speed + wheelSpeedDifference,
                speed - wheelSpeedDifference
        )
    }

    val distanceTraveledMetres: Double
        get() {
            val leftWheelRotations = Math.abs(leftEncoder.distanceTraveledRotations)
            val rightWheelRotations = Math.abs(rightEncoder.distanceTraveledRotations)

            val average = (leftWheelRotations + rightWheelRotations) / 2

            return average * METRES_PER_ROTATION
        }

    private fun speedPolynomial(speed: Double) = Math.sqrt(speed) * 2

    data class WheelSpeeds(val left: Double, val right: Double)
}