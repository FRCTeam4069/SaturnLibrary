package frc.team4069.saturn.lib.util

import kotlinx.coroutines.experimental.Job

typealias SingleListener<T> = suspend (T) -> Unit
typealias TransListener<T> = suspend (from: T, to: T) -> Unit

class StateMachine<T>(val initState: T, val anyState: T? = null) {

    val entryListeners = MultiMap<T, SingleListener<T>>()
    val exitListeners = MultiMap<T, SingleListener<T>>()
    val transitionListeners = MultiMap<Pair<T, T>, TransListener<T>>()

    val whileListenerBlocks = MultiMap<T, Pair<SingleListener<T>, Int>>()
    val whileListeners = MultiMap<T, Job>()

    var currentState = initState

    suspend fun start() {
        entryListeners.filterKeys { it == anyState || it == initState }
            .forEach {  (k, v) ->
                v.forEach { it(k) }
            }
        transitionListeners.filterKeys { (_, v) -> v == anyState || v == initState }
            .forEach { (k, v) ->
                val (from, to) = k
                v.forEach { it(from, to) }
            }
    }

    fun onEnter(state: T, listener: SingleListener<T>) {
        entryListeners.putSingle(state, listener)
    }

    fun onLeave(state: T, listener: SingleListener<T>) {
        exitListeners.putSingle(state, listener)
    }

    fun onTransition(from: T, to: T, listener: TransListener<T>) {
        transitionListeners.putSingle(from to to, listener)
    }

    fun onWhile(state: T, freq: Int = 50, listener: SingleListener<T>) {
        whileListenerBlocks.putSingle(state, listener to freq)
    }

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

            whileListeners.filterKeys { it == currentState }
                .flatMap { (_, v) -> v }
                .forEach {
                    it.cancel()
                }
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

