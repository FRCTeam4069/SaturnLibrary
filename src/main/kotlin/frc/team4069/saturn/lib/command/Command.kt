package frc.team4069.saturn.lib.command

import frc.team4069.saturn.lib.util.disposeOnCancellation
import frc.team4069.saturn.lib.util.launchFrequency
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

abstract class Command(val requiredSubsystems: List<Subsystem>) {
    constructor(vararg subsystems: Subsystem) : this(subsystems.toList())

    /**
     * Dictates when execution of a command should be completed
     */
    abstract val isFinished: Boolean

    /**
     * The frequency at which this command is executed, in hertz.
     */
    var executeFrequency = DEFAULT_FREQUENCY
        protected set

    internal val finishedObservable = Observable.interval((1000 / executeFrequency).toLong(), TimeUnit.MILLISECONDS)
        .map { isFinished }

    internal var state by Delegates.observable(CommandState.READY) { _, _, newValue ->
        observableState.onNext(newValue)
    }

    internal val observableState = PublishSubject.create<CommandState>()

    /** Handle to the executing command */
    private var executor: Job? = null

    internal suspend fun _initialize() {
        state = CommandState.RUNNING
        initialize()
        if(!isFinished && executeFrequency != 0) {
            executor = launchFrequency(executeFrequency, COMMAND_CTX) {
                execute()
            }
        }
    }

    protected open suspend fun initialize() {}
    protected open suspend fun execute() {}
    protected open suspend fun dispose() {}

    internal suspend fun _dispose() {
        executor?.cancelAndJoin()
        executor = null
        dispose()
        state = CommandState.FINISHED
    }


    suspend fun start() {

    }

    suspend fun stop() {

    }

    suspend fun await() = suspendCancellableCoroutine<Unit> { cont ->
        cont.disposeOnCancellation(observableState.filter { it == CommandState.FINISHED }
            .subscribe {
                cont.resume(Unit)
            })
    }

    enum class CommandState {
        READY,
        QUEUED,
        RUNNING,
        FINISHED
    }

    companion object {
        const val DEFAULT_FREQUENCY = 50

        protected val COMMAND_CTX = newFixedThreadPoolContext(2, "Command")
    }
}