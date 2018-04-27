package frc.team4069.robot.commands.drive

import edu.wpi.first.wpilibj.command.PIDCommand
import frc.team4069.robot.subsystems.DriveBaseSubsystem

class DriveStraightCommand(private val distanceMetres: Double, private val speed: Double = 0.5) : PIDCommand(2.0, 0.0, 0.0) {
    override fun initialize() {
        DriveBaseSubsystem.reset()
    }

    override fun usePIDOutput(output: Double) {
        DriveBaseSubsystem.drive(0.0, output)
    }

    override fun isFinished(): Boolean {
        return DriveBaseSubsystem.distanceTraveledMetres >= distanceMetres
    }

    override fun returnPIDInput(): Double {
        return distanceMetres - DriveBaseSubsystem.distanceTraveledMetres
    }

}