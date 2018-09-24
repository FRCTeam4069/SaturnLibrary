package frc.team4069.saturn.lib.util

import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.DisposableHandle
import kotlinx.coroutines.experimental.Job

typealias SingleListener<T> = suspend (T) -> Unit
typealias TransListener<T> = suspend (from: T, to: T) -> Unit

class StateMachine<T>(val initState: T, val anyState: T? = null) {

    val entryListeners = MultiMap<T, SingleListener<T>>()
    val exitListeners = MultiMap<T, SingleListener<T>>()
    val transitionListeners = MultiMap<Pair<T, T>, TransListener<T>>()
    val whileListeners = MultiMap<T, Pair<Job, DisposableHandle>>()

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
        val job = launchFrequency(freq, start = CoroutineStart.LAZY) {
            listener(currentState)
        }

        val handle = object : DisposableHandle {
            override fun dispose() {
                job.cancel()
            }
        }

        whileListeners.putSingle(state, job to handle)
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
                .map { (_, handle) -> handle }
                .forEach(DisposableHandle::dispose)

            whileListeners.filterKeys { it == state }
                .flatMap { (_, v) -> v}
                .map { (job, _) -> job }
                .forEach { it.start() }
        }

        currentState = state
    }
}

