package frc.team4069.saturn.lib.subsystem

import edu.wpi.first.wpilibj.drive.DifferentialDrive
import frc.team4069.saturn.lib.command.Subsystem
import frc.team4069.saturn.lib.motor.SaturnEncoder
import frc.team4069.saturn.lib.motor.SaturnSRX

@Suppress("LeakingThis")
abstract class DifferentialDriveBaseSubsystem : Subsystem() {
    protected abstract val leftDrive: SaturnSRX
    protected abstract val leftEncoder: SaturnEncoder

    protected abstract val rightDrive: SaturnSRX
    protected abstract val rightEncoder: SaturnEncoder

    protected open val metresPerRotation = 0.61

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

    val drive = DifferentialDrive(leftDrive, rightDrive)

    fun stop() = drive.stopMotor()

    fun reset() {
        stop()
        leftEncoder.reset()
        rightEncoder.reset()
    }

    fun drive(turn: Double, speed: Double) {
        drive.curvatureDrive(speed, turn, turn == 0.0)
    }

    val distanceTraveledMetres: Double
        get() {
            val leftWheelRotations = Math.abs(leftEncoder.distanceTraveledRotations)
            val rightWheelRotations = Math.abs(rightEncoder.distanceTraveledRotations)

            val avg = (leftWheelRotations + rightWheelRotations) / 2
            return avg * metresPerRotation
        }
}
