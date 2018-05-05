package frc.team4069.saturn.lib.command

/**
 * Represents a subsystem to be used by Commands
 */
abstract class Subsystem {
    abstract val defaultCommand: Command

    init {
        @Suppress("LeakingThis")
        Scheduler.registerSubsystem(this)
    }

    open fun periodic() {
    }
}