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

val <T> Source<T>.monitor get() = SourceMonitor(this)

class SourceMonitor<T>(
    val source: Source<T>
) {
    var lastValue = source()

    inline fun onChange(block: (T) -> Unit) {
        val newValue = source()
        if (newValue != lastValue) block(newValue)
        lastValue = newValue
    }

    inline fun onWhen(value: T, block: () -> Unit) {
        if (lastValue == value) block()
        onChange { if (it == value) block() }
    }
}

fun SourceMonitor<Boolean>.onChangeToTrue(block: () -> Unit) = onChange { if (it) block() }
fun SourceMonitor<Boolean>.onChangeToFalse(block: () -> Unit) = onChange { if (!it) block() }
fun SourceMonitor<Boolean>.onWhenToTrue(block: () -> Unit) = onWhen(true, block)
fun SourceMonitor<Boolean>.onWhenToFalse(block: () -> Unit) = onWhen(false, block)