package frc.team4069.saturn.lib.command.builtins

import frc.team4069.saturn.lib.command.Command
import java.util.concurrent.TimeUnit

class DelayCommand(delay: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) : Command() {
    constructor(delaySeconds: Long) : this(delaySeconds * 1000, TimeUnit.MILLISECONDS)

    private val timeout = System.currentTimeMillis() + unit.toMillis(delay)

    init {
        executeFrequency = 0
    }

    override val isFinished: Boolean
        get() = System.currentTimeMillis() >= timeout
}