package frc.team4069.saturn.lib.command.builtins

import frc.team4069.saturn.lib.command.Command
import java.util.concurrent.TimeUnit

class DelayCommand(delay: Long, unit: TimeUnit) : Command() {
    constructor(delaySeconds: Double) : this((delaySeconds * 1000).toLong(), TimeUnit.MILLISECONDS)

    private val timeout = System.currentTimeMillis() + unit.toMillis(delay)

    init {
        executeFrequency = 0
        println("Timeout is $timeout")
        println("Current time is ${System.currentTimeMillis()}")
    }

    override val isFinished: Boolean
        get() = System.currentTimeMillis() >= timeout
}