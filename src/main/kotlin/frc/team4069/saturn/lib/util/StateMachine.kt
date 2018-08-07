package frc.team4069.saturn.lib.util

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock
import java.util.concurrent.TimeUnit

typealias Transition<S> = Pair<S, S>
typealias TransitionListener = suspend () -> Unit

class StateMachine<S>(val states: Set<S>, initialState: S, val anyState: S? = null) {
    var currentState = initialState

    val mux = Mutex()
    val transitionListeners = MultiMap<Transition<S>, TransitionListener>()
    val entryListeners = MultiMap<S, TransitionListener>()
    val exitListeners = MultiMap<S, TransitionListener>()
    val whileListeners = mutableListOf<WhileListener>()

    suspend fun onEnter(state: S, listener: TransitionListener) = mux.withLock {
        entryListeners.putSingle(state, listener)
    }

    suspend fun onExit(state: S, listener: TransitionListener) = mux.withLock {
        exitListeners.putSingle(state, listener)
    }

    suspend fun onTransition(oldState: S, newState: S, listener: TransitionListener) = mux.withLock {
        transitionListeners.putSingle(oldState to newState, listener)
    }

    suspend fun onWhile(state: S, frequency: Long = 50, listener: TransitionListener) = mux.withLock {
        whileListeners.add(WhileListener(state, frequency, listener))
    }

    suspend fun start() {
        update(currentState)
        update(anyState ?: return)
    }

    suspend fun update(inState: S) {
        if (inState != currentState) {
            handleTransition(currentState, inState)
        }

        currentState = inState
    }

    private suspend fun handleTransition(old: S, new: S) {
        handleEntry(new)
        handleExit(old)
        mux.withLock {
            transitionListeners.filterKeys { it == old to new }.values.flatten().forEach { it() }
        }
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

    private suspend fun handleExit(old: S) {
        exitListeners.filterKeys { it == old }.values.flatten().forEach { it() }
        whileListeners.filter { it.state == old }.forEach { it.job.cancelAndJoin() }
    }

    inner class WhileListener(val state: S, val frequency: Long, val listener: suspend () -> Unit) {
        lateinit var job: Job
    }
}