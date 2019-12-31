/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package frc.team4069.saturn.lib.util

/* ktlint-disable no-wildcard-imports */
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.derived.Hertz
import frc.team4069.saturn.lib.mathematics.units.hertz
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launchFrequency(
        frequency: SIUnit<Hertz> = 50.hertz,
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