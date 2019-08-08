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

package frc.team4069.saturn.lib.hid

import kotlin.math.absoluteValue


class HIDButton(private val source: HIDSource,
                private val threshold: Double,
                private val whileOff: List<HIDControlListener>,
                private val whileOn: List<HIDControlListener>,
                private val changeOn: List<HIDControlListener>,
                private val changeOff: List<HIDControlListener>) : HIDControl {

    companion object {
        const val DEFAULT_THRESHOLD = 0.5
    }

    private var lastValue = source().absoluteValue >= threshold

    override fun update() {
        val newValue = source().absoluteValue >= threshold
        if (lastValue != newValue) {
            // Value has changed
            if (newValue) {
                changeOn.forEach { it() }
            } else {
                changeOff.forEach { it() }
            }
        } else {
            // Value stayed the same
            if (newValue) {
                whileOn.forEach { it() }
            } else {
                whileOff.forEach { it() }
            }
        }
        lastValue = newValue
    }
}

typealias HIDControlListener = () -> Unit

interface HIDControl {
    fun update()
}
