package frc.team4069.saturn.lib.util

import kotlinx.coroutines.experimental.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

typealias Transition<S> = Pair<S, S>
typealias TransitionListener = suspend () -> Unit

class StateMachine<S>(val states: Set<S>, initialState: S, val anyState: S? = null) {

    var currentState: S by Delegates.observable(initialState) { _, old, new ->
        runBlocking {
            handleTransition(old, new)
        }
    }

    private val transitionListeners = MultiMap<Transition<S>, TransitionListener>()
    private val entryListeners = MultiMap<S, TransitionListener>()
    private val exitListeners = MultiMap<S, TransitionListener>()
    private val whileListeners = mutableListOf<WhileListener>()

    fun onEnter(state: S, listener: TransitionListener) {
        entryListeners.putSingle(state, listener)
    }

    fun onExit(state: S, listener: TransitionListener) {
        exitListeners.putSingle(state, listener)
    }

    fun onTransition(oldState: S, newState: S, listener: TransitionListener) {
        transitionListeners.putSingle(oldState to newState, listener)
    }

    fun onWhile(state: S, frequency: Long = 50, listener: TransitionListener) {
        whileListeners.add(WhileListener(state, frequency, listener))
    }

    suspend fun start() {
        update(anyState)
        handleTransition(null, currentState)
    }

    fun update(inState: S?) {
        if (inState != currentState && inState != null) {
            currentState = inState
        }
    }

    private suspend fun handleTransition(old: S?, new: S) {
        handleEntry(new)
        handleExit(old)
        transitionListeners.filterKeys { it == old to new }.values.flatten().forEach { it() }
    }

    private suspend fun handleEntry(new: S) {
        entryListeners.filterKeys { it == new }.values.flatten().forEach { it() }

        whileListeners.filter { it.state == new || it.state == anyState }.forEach { listener ->
            listener.job = launch {
                val frequency = listener.frequency
                if (frequency < 0) throw IllegalArgumentException("While frequency cannot be negative!")
                val timeBetweenUpdate = TimeUnit.SECONDS.toNanos(1) / frequency

                var nextNS = System.nanoTime() + timeBetweenUpdate
                while (isActive) {
                    listener.listener()

                    val delayNeeded = nextNS - System.nanoTime()
                    nextNS += timeBetweenUpdate
                    delay(delayNeeded, TimeUnit.NANOSECONDS)
                }
            }
        }
    }

    private suspend fun handleExit(old: S?) {
        exitListeners.filterKeys { it == old }.values.flatten().forEach { it() }
        whileListeners.filter { it.state == old }.forEach { it.job.cancelAndJoin() }
    }

    inner class WhileListener(val state: S, val frequency: Long, val listener: suspend () -> Unit) {
        lateinit var job: Job
    }
}