package frc.team4069.saturn.lib.subsystem

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.wpilibj.Timer
import frc.team4069.saturn.lib.commands.SaturnCommand

class CharacterizationCommand(val driveSubsystem: TankDriveSubsystem) : SaturnCommand(driveSubsystem) {
    val speed = NetworkTableInstance.getDefault().getEntry("/robot/autospeed")
    val telemEntry = NetworkTableInstance.getDefault().getEntry("/robot/telemetry")

    var priorSpd = 0.0
    val telemetry = Array<Number>(9) { 0 }

    override suspend fun execute() {
        val now = Timer.getFPGATimestamp()

        val lpos = driveSubsystem.leftMotor.sensorPosition.meter
        val lvel = driveSubsystem.leftMotor.sensorVelocity.value

        val rpos = driveSubsystem.rightMotor.sensorPosition.meter
        val rvel = driveSubsystem.rightMotor.sensorVelocity.value

        val battery = RobotController.getBatteryVoltage()

        val lvolt = driveSubsystem.leftMotor.motorOutputVoltage.value
        val rvolt = driveSubsystem.rightMotor.motorOutputVoltage.value

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