package frc.team4069.saturn.lib.command.builtins

import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.Condition

abstract class InstantCommand : Command() {
    init {
        finishCondition += Condition.TRUE
    }
}

class InstantRunnableCommand(private val runnable: suspend () -> Unit) : InstantCommand() {
    override suspend fun initialize() {
        super.initialize()
        runnable()
    }
}

