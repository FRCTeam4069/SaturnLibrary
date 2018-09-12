package frc.team4069.saturn.lib.command

abstract class Command(vararg val requiredSubsystems: Subsystem) {

    /**
     * Called when the command is first constructed.
     *
     * Should contain code to prepare for command looping
     */
    open suspend fun initialize() {}

    /**
     * Called at [updateFrequency]hz during the lifetime of the command
     */
    open suspend fun execute() {}

    /**
     * Called either when [isFinished] returns true, or the command was canceled. After this function is called the command is discarded
     * The state of the command is encapsulated in [type]
     *
     * Tear down code should go here.
     */
    open suspend fun dispose(type: FinishType) {}

    /**
     * Predicate referenced when checking if the command is complete and ready for disposal
     */
    abstract val isFinished: Boolean

    /**
     * The frequency at which the command will be called, in hertz
     */
    open val updateFrequency: Int = 50

    enum class FinishType {
        NORMAL,
        CANCELED
    }
}