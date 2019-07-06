package frc.team4069.saturn.lib.subsystem

import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import frc.team4069.saturn.lib.commands.SaturnSubsystem
import frc.team4069.saturn.lib.debug.LiveDashboard
import frc.team4069.saturn.lib.localization.Localization
import frc.team4069.saturn.lib.mathematics.twodim.control.TrajectoryTracker
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.Rotation2d
import frc.team4069.saturn.lib.motor.SaturnMotor
import frc.team4069.saturn.lib.util.Source
import kotlin.math.absoluteValue
import kotlin.math.max

abstract class TankDriveSubsystem : SaturnSubsystem() {
    private var quickStopAccumulator = 0.0

    abstract val localization: Localization

    abstract val leftMotor: SaturnMotor<Length>
    abstract val rightMotor: SaturnMotor<Length>

    abstract val gyro: Source<Rotation2d>

    abstract val trajectoryTracker: TrajectoryTracker
    abstract val driveModel: DifferentialDriveModel

    var robotPosition
        get() = localization.robotPosition
        set(value) = localization.reset(value)

    override fun lateInit() {
        localization.reset()
        Notifier {
            localization.update()
        }.startPeriodic(1.0 / 100.0)
    }

    override fun setNeutral() {
        tankDrive(0.0, 0.0)
    }

    override fun periodic() {
        LiveDashboard.robotHeading = robotPosition.rotation.radian
        LiveDashboard.robotX = robotPosition.translation.x.feet
        LiveDashboard.robotY = robotPosition.translation.y.feet
    }

    abstract fun setOutput(output: TrajectoryTracker.TrajectoryTrackerOutput)

    fun curvatureDrive(
            linearPercent: Double,
            curvaturePercent: Double,
            isQuickTurn: Boolean
    ) {
        val angularPower: Double
        val overPower: Boolean

        if (isQuickTurn) {
            if (linearPercent.absoluteValue < kQuickStopThreshold) {
                quickStopAccumulator = (1 - kQuickStopAlpha) * quickStopAccumulator +
                        kQuickStopAlpha * curvaturePercent.coerceIn(-1.0, 1.0) * 2.0
            }
            overPower = true
            angularPower = curvaturePercent
        } else {
            overPower = false
            angularPower = linearPercent.absoluteValue * curvaturePercent - quickStopAccumulator

            when {
                quickStopAccumulator > 1 -> quickStopAccumulator -= 1.0
                quickStopAccumulator < -1 -> quickStopAccumulator += 1.0
                else -> quickStopAccumulator = 0.0
            }
        }

        var leftMotorOutput = linearPercent + angularPower
        var rightMotorOutput = linearPercent - angularPower

        // If rotation is overpowered, reduce both outputs to within acceptable range
        if (overPower) {
            when {
                leftMotorOutput > 1.0 -> {
                    rightMotorOutput -= leftMotorOutput - 1.0
                    leftMotorOutput = 1.0
                }
                rightMotorOutput > 1.0 -> {
                    leftMotorOutput -= rightMotorOutput - 1.0
                    rightMotorOutput = 1.0
                }
                leftMotorOutput < -1.0 -> {
                    rightMotorOutput -= leftMotorOutput + 1.0
                    leftMotorOutput = -1.0
                }
                rightMotorOutput < -1.0 -> {
                    leftMotorOutput -= rightMotorOutput + 1.0
                    rightMotorOutput = -1.0
                }
            }
        }

        // Normalize the wheel speeds
        val maxMagnitude = max(leftMotorOutput.absoluteValue, rightMotorOutput.absoluteValue)
        if (maxMagnitude > 1.0) {
            leftMotorOutput /= maxMagnitude
            rightMotorOutput /= maxMagnitude
        }

        tankDrive(leftMotorOutput, rightMotorOutput)
    }

    open fun tankDrive(left: Double, right: Double) {
        leftMotor.setDutyCycle(left, 0.0)
        rightMotor.setDutyCycle(right, 0.0)
    }

    companion object {
        const val kQuickStopAlpha = DifferentialDrive.kDefaultQuickStopAlpha
        const val kQuickStopThreshold = DifferentialDrive.kDefaultQuickStopThreshold
    }
}
