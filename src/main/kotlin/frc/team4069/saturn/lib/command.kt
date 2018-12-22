package frc.team4069.saturn.lib

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.InstantCommand

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