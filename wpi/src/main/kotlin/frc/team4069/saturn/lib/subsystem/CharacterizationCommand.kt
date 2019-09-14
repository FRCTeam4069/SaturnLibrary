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

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.wpilibj.Timer
import frc.team4069.saturn.lib.commands.SaturnCommand
import frc.team4069.saturn.lib.mathematics.units.conversions.meter

class CharacterizationCommand(val driveSubsystem: TankDriveSubsystem) : SaturnCommand(driveSubsystem) {
    val speed = NetworkTableInstance.getDefault().getEntry("/robot/autospeed")
    val telemEntry = NetworkTableInstance.getDefault().getEntry("/robot/telemetry")

    var priorSpd = 0.0
    val telemetry = Array<Number>(9) { 0 }

    override fun execute() {
        val now = Timer.getFPGATimestamp()

        val lpos = driveSubsystem.leftMotor.encoder.position.meter
        val lvel = driveSubsystem.leftMotor.encoder.velocity.value

        val rpos = driveSubsystem.rightMotor.encoder.position.meter
        val rvel = driveSubsystem.rightMotor.encoder.velocity.value

        val battery = RobotController.getBatteryVoltage()

        val lvolt = driveSubsystem.leftMotor.voltageOutput
        val rvolt = driveSubsystem.rightMotor.voltageOutput

        val spd = speed.getDouble(0.0)
        priorSpd = spd

        driveSubsystem.tankDrive(spd, spd)

        telemetry[0] = now
        telemetry[1] = battery
        telemetry[2] = spd
        telemetry[3] = lvolt
        telemetry[4] = rvolt
        telemetry[5] = lpos
        telemetry[6] = rpos
        telemetry[7] = lvel
        telemetry[8] = rvel

        telemEntry.setNumberArray(telemetry)
    }
}