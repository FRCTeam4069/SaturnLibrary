package frc.team4069.saturn.lib.subsystem.drive.swerve

import frc.team4069.saturn.lib.commands.SaturnSubsystem
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.radian
import frc.team4069.saturn.lib.sensors.SaturnPigeon
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * An experimental drive subsystem for a swerve drive drivetrain
 *
 * Swerve drive consists of 4 independently rotated wheels with associated linear motors.
 * This control scheme allows for motion in any direction without giving up pushing power as would occur
 * with different holonomic control schemes.
 */
abstract class SwerveDriveSubsystem : SaturnSubsystem("Drive Subsystem") {

    abstract val frontLeftModule: SwerveModule
    abstract val frontRightModule: SwerveModule
    abstract val backLeftModule: SwerveModule
    abstract val backRightModule: SwerveModule

    abstract val gyro: SaturnPigeon

    abstract val wheelBase: Length
    abstract val trackWidth: Length

    /**
     * Function to update motor outputs with the given control values
     */
    fun swerveDrive(forward: Double, turn: Double, strafe: Double) {
        val angle = gyro.fusedHeading

        val fwd = forward * angle.cos - strafe * angle.sin
        val str = forward * angle.sin + strafe * angle.cos

        val l = wheelBase.meter
        val w = trackWidth.meter
        val r = sqrt(l.pow(2) + w.pow(2))

        val a = str - turn * (l / r)
        val b = str + turn * (l / r)
        val c = fwd - turn * (w / r)
        val d = fwd + turn * (w / r)

        val speed1 = sqrt(b*b + c*c)
        val speed2 = sqrt(b*b + d*d)
        val speed3 = sqrt(a*a + d*d)
        val speed4 = sqrt(a*a + c*c)

        val (ws1, ws2, ws3, ws4) = normalize(speed1, speed2, speed3, speed4)
        val wa1 = atan2(b, c).radian
        val wa2 = atan2(b, d).radian
        val wa3 = atan2(a, d).radian
        val wa4 = atan2(a, c).radian

        frontRightModule.setOutputs(SwerveModule.SwerveCommand(ws1, wa1))
        frontLeftModule.setOutputs(SwerveModule.SwerveCommand(ws2, wa2))
        backLeftModule.setOutputs(SwerveModule.SwerveCommand(ws3, wa3))
        backRightModule.setOutputs(SwerveModule.SwerveCommand(ws4, wa4))
    }

    private fun normalize(speed1: Double, speed2: Double, speed3: Double, speed4: Double): WheelSpeeds {
        val max = max(speed1, max(speed2, max(speed3, speed4)))
        return if(max > 1.0) {
            WheelSpeeds(speed1 / max, speed2 / max, speed3 / max, speed4 / max)
        }else {
            WheelSpeeds(speed1, speed2, speed3, speed4)
        }
    }

    private data class WheelSpeeds(val ws1: Double, val ws2: Double, val ws3: Double, val ws4: Double)
}
