package frc.team4069.saturn.lib.command.builtins

import frc.team4069.saturn.lib.command.Command

abstract class InstantCommand : Command() {
    override val isFinished = true

    init {
        executeFrequency = 0
    }
}

class InstantRunnableCommand(private val block: suspend () -> Unit) : InstantCommand() {
    override suspend fun initialize() = block()
}

class PeriodicRunnableCommand(private val block: suspend () -> Unit,
                              private val exitCondition: Boolean,
                              freq: Int = DEFAULT_FREQUENCY) : Command() {
    override val isFinished = exitCondition

    init {
        this.executeFrequency = freq
    }

    override suspend fun execute() = block()
}
