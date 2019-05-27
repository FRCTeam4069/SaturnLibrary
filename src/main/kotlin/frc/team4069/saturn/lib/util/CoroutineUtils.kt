package frc.team4069.saturn.lib.util

/* ktlint-disable no-wildcard-imports */
import edu.wpi.first.hal.NotifierJNI
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Frequency
import frc.team4069.saturn.lib.mathematics.units.derivedunits.hertz
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
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
    frequency: Int = 50,
    block: suspend CoroutineScope.() -> Unit
) {
    val timeBetweenUpdate = TimeUnit.SECONDS.toMicros(1) / frequency
    val notifier = NotifierJNI.initializeNotifier()

    var nextUs = TimeUnit.NANOSECONDS.toMicros(System.nanoTime()) + timeBetweenUpdate

    while(isActive) {
        block(this)
        nextUs += timeBetweenUpdate
        NotifierJNI.updateNotifierAlarm(notifier, nextUs)
        NotifierJNI.waitForNotifierAlarm(notifier)
    }

    NotifierJNI.cleanNotifier(notifier)
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