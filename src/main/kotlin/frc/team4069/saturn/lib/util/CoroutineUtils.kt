package frc.team4069.saturn.lib.util

/* ktlint-disable no-wildcard-imports */
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launchFrequency(
    frequency: Int = 50,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    if (frequency <= 0) throw IllegalArgumentException("Frequency cannot be lower then 1!")
    return launch(context, start) {
        loopFrequency(frequency, block)
    }
}

suspend fun CoroutineScope.loopFrequency(
    frequency: Int = 50,
    block: suspend CoroutineScope.() -> Unit
) {
    val timeBetweenUpdate = TimeUnit.SECONDS.toMillis(1) / frequency
    // Stores when the next update should happen
    var nextMS = System.currentTimeMillis() + timeBetweenUpdate
    while (isActive) {
        block(this)
        val delayNeeded = nextMS - System.nanoTime()
        nextMS += timeBetweenUpdate
        delay(delayNeeded)
    }
}

inline fun disposableHandle(crossinline block: () -> Unit) = object : DisposableHandle {
    override fun dispose() {
        block()
    }
}

fun disposableHandle(vararg handles: DisposableHandle) = disposableHandle(handles.asList())

fun disposableHandle(handles: Collection<DisposableHandle>) = disposableHandle {
    handles.forEach { it.dispose() }
}