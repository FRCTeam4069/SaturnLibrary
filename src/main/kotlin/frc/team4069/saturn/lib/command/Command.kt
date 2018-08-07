package frc.team4069.saturn.lib.command

import kotlinx.coroutines.experimental.DisposableHandle
import kotlinx.coroutines.experimental.disposeOnCancellation
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import java.util.concurrent.CopyOnWriteArrayList

abstract class Command(_freq: Int = DEFAULT_FREQUENCY) {
    var updateFrequency = _freq
        protected set

    internal open val requiredSubsystems = mutableListOf<Subsystem>()
    internal val completionListeners = CopyOnWriteArrayList<suspend (Command) -> Unit>()

    protected val finishCondition = CommandCondition(Condition.FALSE)
    internal val exposedCondition: Condition
        get() = finishCondition

    internal var state = State.READY

    suspend fun isFinished() = finishCondition.isMet()

    protected operator fun Subsystem.unaryPlus() = requires(this)
    protected fun requires(subsystem: Subsystem) = requiredSubsystems.add(subsystem)

    open suspend fun initialize() {}
    open suspend fun execute() {}
    open suspend fun dispose() {}

    suspend fun start() {
        CommandHandler.start(this)
    }

    suspend fun stop() = CommandHandler.stop(this)

    fun invokeOnCompletion(block: suspend (Command) -> Unit): DisposableHandle {
        completionListeners.add(block)
        return object : DisposableHandle {
            override fun dispose() {
                completionListeners.remove(block)
            }
        }
    }

    suspend fun await() = suspendCancellableCoroutine<Unit> { cont ->
        cont.disposeOnCancellation(invokeOnCompletion {
            cont.resume(Unit)
        })
    }

    protected class CommandCondition(currentCondition: Condition) : Condition() {
        private val listener: suspend (Condition) -> Unit = { invokeCompletionListeners() }
        private var handle = currentCondition.invokeOnCompletion(listener)

        private var currentCondition = currentCondition
            set(value) {
                handle.dispose()
                handle = value.invokeOnCompletion(listener)
                field = value
            }

        override suspend fun isMet() = currentCondition.isMet()

        operator fun plusAssign(cond: Condition) {
            currentCondition = currentCondition or cond
        }
    }

    enum class State(val finished: Boolean) {
        READY(false),
        RUNNING(false),
        FINISHED(true),
        CANCELLED(true)
    }

    companion object {
        const val DEFAULT_FREQUENCY = 50
    }
}