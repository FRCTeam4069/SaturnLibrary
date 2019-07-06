package frc.team4069.saturn.lib.util

/* ktlint-disable no-wildcard-imports */
import frc.team4069.saturn.lib.mathematics.units.Frequency
import frc.team4069.saturn.lib.mathematics.units.hertz
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launchFrequency(
    frequency: Frequency = 50.hertz,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    if (frequency.value <= 0) throw IllegalArgumentException("Frequency cannot be lower then 1!")
    return launch(context, start) {
        loopFrequency(frequency.value.toInt(), block)
    }
}


suspend fun CoroutineScope.loopFrequency(
    frequency: Int,
    block: suspend CoroutineScope.() -> Unit
) {
    val notifier = SaturnNotifier(frequency)

    notifier.updateAlarm()

    while(isActive) {
        notifier.waitForAlarm()
        block(this)
        notifier.updateAlarm()
    }

    notifier.close()
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