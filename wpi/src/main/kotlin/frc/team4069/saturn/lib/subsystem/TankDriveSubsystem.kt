/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package frc.team4069.saturn.lib.subsystem

import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import frc.team4069.saturn.lib.commands.SaturnSubsystem
import frc.team4069.saturn.lib.debug.LiveDashboard
import frc.team4069.saturn.lib.localization.Localization
import frc.team4069.saturn.lib.mathematics.twodim.control.TrajectoryTracker
import frc.team4069.saturn.lib.mathematics.twodim.control.TrajectoryTrackerOutput
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.units.Meter
import frc.team4069.saturn.lib.mathematics.units.conversions.feet
import frc.team4069.saturn.lib.mathematics.units.volt
import frc.team4069.saturn.lib.motor.SaturnMotor
import frc.team4069.saturn.lib.util.Source
import kotlin.math.absoluteValue
import kotlin.math.max

abstract class TankDriveSubsystem : SaturnSubsystem() {
    private var quickStopAccumulator = 0.0

    abstract val localization: Localization

    abstract val leftMotor: SaturnMotor<Meter>
    abstract val rightMotor: SaturnMotor<Meter>

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

    abstract fun setOutput(output: TrajectoryTrackerOutput)

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
        leftMotor.setDutyCycle(left, 0.volt)
        rightMotor.setDutyCycle(right, 0.volt)
    }

    companion object {
        const val kQuickStopAlpha = DifferentialDrive.kDefaultQuickStopAlpha
        const val kQuickStopThreshold = DifferentialDrive.kDefaultQuickStopThreshold
    }
}
