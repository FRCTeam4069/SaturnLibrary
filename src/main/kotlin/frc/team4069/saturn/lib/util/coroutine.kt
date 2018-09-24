package frc.team4069.saturn.lib.util

import edu.wpi.first.wpilibj.Timer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext

fun launchFrequency(
    frequency: Int = 50,
    context: CoroutineContext = CommonPool,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    parent: Job? = null,
    onCompletion: CompletionHandler? = null,
    block: suspend CoroutineScope.() -> Unit
    ): Job {

    return launch(context, start, parent, onCompletion) {
        val deltaTime = 1000 / frequency // Delay (in ms)

        // Use this only if necessary
        val next = Timer.getFPGATimestamp() + deltaTime
        while(isActive) {
            block()
//            val delay = next - Timer.getFPGATimestamp()
            delay(deltaTime)
        }
    }
}

fun CancellableContinuation<*>.disposeOnCancellation(handle: Disposable) {
    invokeOnCancellation {
        handle.dispose()
    }
}