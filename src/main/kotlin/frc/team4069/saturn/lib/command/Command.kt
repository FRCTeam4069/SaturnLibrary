package frc.team4069.saturn.lib.command

/**
 * Represents an action, or a set of actions to be completed on a subsystem
 */
abstract class Command {
    internal val requiredSystems = mutableSetOf<Subsystem>()

    /**
     * Used to mark a subsystem as required to execute this command.
     *
     * Queueing a command that requires a subsystem already in use will suspend the older command until the completion of the younger command.
     */
    protected fun requires(subsystem: Subsystem) {
        requiredSystems.add(subsystem)
    }

    /**
     * Called at the beginning of the command's lifecycle, when it has just been queued in the [Scheduler]
     */
    open fun initialize() {

    }

    /**
     * Called frequently in the body of the command. Used to update the subsystem periodically
     */
    open fun periodic() {

    }

    /**
     * Called when a command is suspended to allow a younger command to run
     */
    open fun suspended() {

    }

    /**
     * Called when a suspended command is able to return to the scheduler
     */
    open fun resumed() {

    }

    /**
     * Called when [isFinished] returns true. Tail end of the command lifecycle. Reference is discarded after this is called
     */
    open fun finished() {

    }

    /**
     * Called when a Command is terminated by the scheduler without [isFinished] necessarily being true.
     */
    open fun cancelled() {

    }

    /**
     * Represents the end state of the command. When the return value is true, the command will be finished and discarded in the next iteration of the [Scheduler]
     */
    abstract val isFinished: Boolean
}