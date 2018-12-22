package frc.team4069.saturn.lib

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.command.Subsystem

/**
 * Miscellaneous utils for commands
 */

abstract class SaturnSubsystem : Subsystem() {

    /**
     * Creates a command that encapsulates an automated systems check on this subsystem
     *
     * These commands can be compounded in the robot class for automated systems check in test mode
     */
    abstract fun subsystemTest(): Command
}

fun sequential(vararg commands: Command) = object : CommandGroup() {
    init {
        for(command in commands) {
            addSequential(command)
        }
    }
}

inline fun command(crossinline block: () -> Unit) = object : InstantCommand() {
    override fun initialize() {
        block()
    }
}