package frc.team4069.saturn.lib.command

import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock

open class CommandTask(val command: Command, private val onFinish: (CommandTask) -> Unit) {

    private val finishMux = Mutex()
    private var state = State.CREATED
    private var finished = false
    lateinit var completeHandle: Disposable

    suspend fun _start() {
        assert(state == State.CREATED) { "You cannot reuse tasks." }
        state = State.RUNNING

        command._initialize()
        completeHandle = command.finishedObservable.filter { it }
            .subscribe { _ ->
                runBlocking {
                    finishMux.withLock {
                        assert(!finished) { "Got finish event twice" }
                        finished = true

                        onFinish(this@CommandTask)
                    }
                }
            }
    }

    suspend fun _stop() {
        completeHandle.dispose()
        finishMux.withLock {
            assert(state == State.RUNNING) { "Tried to stop a task that wasn't running" }
            state = State.STOPPED
            command._dispose()
            stop()
        }
    }

    protected open suspend fun stop() {}

    enum class State {
        CREATED,
        RUNNING,
        STOPPED
    }
}