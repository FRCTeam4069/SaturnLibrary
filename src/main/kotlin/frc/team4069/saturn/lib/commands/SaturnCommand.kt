package frc.team4069.saturn.lib.commands

import edu.wpi.first.wpilibj.experimental.command.SendableCommandBase
import edu.wpi.first.wpilibj.experimental.command.Subsystem

abstract class SaturnCommand(vararg subsystems: Subsystem) : SendableCommandBase() {
    init {
        addRequirements(*subsystems)
    }
}
