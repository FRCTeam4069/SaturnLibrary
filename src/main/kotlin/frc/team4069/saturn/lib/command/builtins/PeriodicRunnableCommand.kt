package frc.team4069.saturn.lib.command.builtins

import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.Condition

class PeriodicRunnableCommand(
        private val runnable: suspend () -> Unit,
        exitCondition: Condition,
        updateFrequency: Int = Command.DEFAULT_FREQUENCY) : Command(updateFrequency) {
    init {
        finishCondition += exitCondition
    }

    override suspend fun execute() = runnable()
}

