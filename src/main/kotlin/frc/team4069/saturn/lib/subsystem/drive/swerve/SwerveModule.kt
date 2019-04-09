package frc.team4069.saturn.lib.subsystem.drive.swerve

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.Rotation2d
import frc.team4069.saturn.lib.motor.SaturnSRX

class SwerveModule(val speedMotor: SaturnSRX<Length>, val steerMotor: SaturnSRX<Rotation2d>) {

    fun setOutputs(command: SwerveCommand) {
        speedMotor.set(ControlMode.PercentOutput, command.speed)
        steerMotor.set(ControlMode.MotionMagic, command.angle)
    }

    data class SwerveCommand(val speed: Double, val angle: Rotation2d)
}