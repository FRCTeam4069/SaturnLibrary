package frc.team4069.saturn.lib.command

import frc.team4069.saturn.lib.util.launchFrequency
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.newFixedThreadPoolContext

abstract class Command(val requiredSubsystems: List<Subsystem>) {

    var frequency = DEFAULT_FREQUENCY
        protected set

    internal var state = State.READY

    open suspend fun initialize() {}

    open suspend fun execute() {}

    open suspend fun dispose() {}

    abstract val isFinished: Boolean

    private var executor: Job? = null

    internal suspend fun initialize0() {
        initialize()
        state = State.RUNNING

        if(!isFinished) {
            executor = launchFrequency(frequency, COMMAND_CTX) {
                execute()
            }
        }
    }

    internal suspend fun dispose0() {
        executor?.cancelAndJoin()
        executor = null
        dispose()

        state = if(isFinished) {
            State.FINISHED
        }else {
            State.CANCELED
        }
    }

    suspend fun start() {

    }

    constructor(vararg subsystems: Subsystem) : this(subsystems.toList())

    companion object {
        const val DEFAULT_FREQUENCY = 50
        protected val COMMAND_CTX = newFixedThreadPoolContext(2, "Command")
    }

    enum class State {
        READY,
        QUEUED,
        RUNNING,
        FINISHED,
        CANCELED
    }
}