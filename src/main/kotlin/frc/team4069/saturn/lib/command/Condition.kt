package frc.team4069.saturn.lib.command

import kotlinx.coroutines.experimental.DisposableHandle
import java.util.concurrent.CopyOnWriteArrayList

inline fun condition(crossinline block: suspend () -> Boolean) = object : Condition() {
    override suspend fun isMet() = block()
}

fun condition(command: Command) = object : Condition() {
    init {
        command.invokeOnCompletion {
            invokeCompletionListeners()
        }
    }

    override suspend fun isMet() = command.didComplete
}

abstract class Condition {

    private val completionListeners = CopyOnWriteArrayList<suspend (Condition) -> Unit>()

    protected suspend fun invokeCompletionListeners() {
        for(listener in completionListeners) {
            listener(this)
        }
    }

    abstract suspend fun isMet(): Boolean

    fun invokeOnCompletion(block: suspend (Condition) -> Unit): DisposableHandle {
        completionListeners.add(block)

        return object : DisposableHandle {
            override fun dispose() {
                completionListeners.remove(block)
            }
        }
    }

    infix fun or(other: Condition) = conditionGroup(this, other) { a, b -> a || b}
    infix fun or(block: suspend () -> Boolean) = this or condition(block)
    infix fun or(command: Command) = this or condition(command)

    infix fun and(other: Condition) = conditionGroup(this, other) { a, b -> a && b}
    infix fun and(block: suspend () -> Boolean) = this and condition(block)
    infix fun and(command: Command) = this and condition(command)

    private inline fun conditionGroup(firstCondition: Condition, secondCondition: Condition, crossinline condition: (Boolean, Boolean) -> Boolean) = object : Condition() {
        init {
            firstCondition.invokeOnCompletion {
                if(condition(true, secondCondition.isMet())) {
                    invokeCompletionListeners()
                }
            }
            secondCondition.invokeOnCompletion {
                if(condition(firstCondition.isMet(), true)) {
                    invokeCompletionListeners()
                }
            }
        }

        override suspend fun isMet() = condition(firstCondition.isMet(), secondCondition.isMet())
    }

    companion object {
        val TRUE = -true
        val FALSE = -false
    }
}

operator fun Boolean.unaryMinus(): Condition = condition { this }