package frc.team4069.saturn.lib.util

import kotlinx.coroutines.experimental.Job

typealias SingleListener<T> = suspend (T) -> Unit
typealias TransListener<T> = suspend (from: T, to: T) -> Unit

/**
 * Implementation of a state machine encapsulating the current state of a system,
 * as well as actions to take for various states and transitions between.
 */
class StateMachine<T>(val initState: T, val anyState: T? = null) {

    /**
     * Contains actions to be called when the machine enters a given state
     */
    val entryListeners = MultiMap<T, SingleListener<T>>()

    /**
     * Contains actions to be called when the machine exits a given state
     */
    val exitListeners = MultiMap<T, SingleListener<T>>()
    /**
     * Contains actions to be called when the machine transitions from one specified state to another
     */
    val transitionListeners = MultiMap<Pair<T, T>, TransListener<T>>()

    /**
     * Contains actions to be called while the machine is in a given state
     */
    val whileListenerBlocks = MultiMap<T, Pair<SingleListener<T>, Int>>()
    /**
     * Contains handles to actions currently being run
     */
    val whileListeners = MultiMap<T, Job>()

    var currentState = initState

    /**
     * Initializes the state machine. Calls listeners which should be running in the initial state of the machine, or for any state of the machine
     */
    suspend fun start() {
        entryListeners.filterKeys { it == anyState || it == initState }
            .forEach {  (k, v) ->
                v.forEach { it(k) }
            }
        whileListenerBlocks.filter { it == anyState || it == initState }
            .forEach { (state, v) ->
                v.forEach { (listener, freq) ->
                    whileListeners.putSingle(state, launchFrequency(frequency = freq) { listener(state) })
                }
            }
        transitionListeners.filterKeys { (_, v) -> v == anyState || v == initState }
            .forEach { (k, v) ->
                val (from, to) = k
                v.forEach { it(from, to) }
            }
    }

    /**
     * Adds an action to be performed when the machine enters [state]
     */
    fun onEnter(state: T, listener: SingleListener<T>) {
        entryListeners.putSingle(state, listener)
    }

    /**
     * Adds an action to be performed when the machine exits [state]
     */
    fun onLeave(state: T, listener: SingleListener<T>) {
        exitListeners.putSingle(state, listener)
    }

    /**
     * Adds an action to be performed when the machine transitions from [from] to [to]
     */
    fun onTransition(from: T, to: T, listener: TransListener<T>) {
        transitionListeners.putSingle(from to to, listener)
    }

    /**
     * Adds an action to be run at [freq] hz while the machine is in [state]
     */
    fun onWhile(state: T, freq: Int = 50, listener: SingleListener<T>) {
        whileListenerBlocks.putSingle(state, listener to freq)
    }

    /**
     * Feeds the machine a new value for [state], and updates running actions accordingly.
     */
    suspend fun feed(state: T) {
        if(state != currentState) {
            exitListeners.filterKeys { it == currentState }
                .flatMap { (_, v) -> v }
                .forEach { it(state) }
            entryListeners.filterKeys { it == state }
                .flatMap { (_, v) -> v }
                .forEach { it(state) }

            transitionListeners.filterKeys { (from, to) -> from == currentState && to == state }
                .flatMap { (_, v) -> v }
                .forEach { it(currentState, state) }

            //TODO: This is broken, will clear handles to anyState
            whileListeners.filterKeys { it == currentState }
                .flatMap { (_, v) -> v }
                .forEach { it.cancel() }
            whileListeners.clear()

            whileListenerBlocks.filterKeys { it == state }
                .flatMap { (_, v) -> v }
                .forEach { (listener, freq) ->
                    whileListeners.putSingle(state, launchFrequency(freq) {
                        listener(state)
                    })
                }
        }

        currentState = state
    }
}

