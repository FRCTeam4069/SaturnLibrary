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

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj2.command.Command
import frc.team4069.saturn.lib.util.BooleanSource
import kotlin.properties.Delegates

enum class POVSide(val angle: Int) {
    UP(0),
    DOWN(180),
    LEFT(270),
    RIGHT(90)
}

fun <T : GenericHID> controller(genericHID: T, block: SaturnHIDBuilder<T>.() -> Unit): SaturnHID<T> {
    return SaturnHIDBuilder(genericHID).apply(block).build()
}

class SaturnHIDBuilder<T : GenericHID>(private val genericHID: T) {
    private val controlBuilders = mutableListOf<SaturnHIDControlBuilder>()
    private val stateControlBuilders = mutableMapOf<BooleanSource, SaturnHIDBuilder<T>>()

    fun button(buttonId: Int, block: SaturnHIDButtonBuilder.() -> Unit = {}) =
        control(HIDButtonSource(genericHID, buttonId), block = block)

    fun axisButton(
        axisId: Int,
        threshold: Double = HIDButton.DEFAULT_THRESHOLD,
        block: SaturnHIDButtonBuilder.() -> Unit = {}
    ) = control(HIDAxisSource(genericHID, axisId), threshold, block)

    fun pov(angle: POVSide, block: SaturnHIDButtonBuilder.() -> Unit = {}) = pov(0, angle, block)
    fun pov(pov: Int, angle: POVSide, block: SaturnHIDButtonBuilder.() -> Unit = {}) =
        control(HIDPOVSource(genericHID, pov, angle.angle), block = block)

    fun state(state: BooleanSource, block: SaturnHIDBuilder<T>.() -> Unit) =
            stateControlBuilders.compute(state) { _, _ -> SaturnHIDBuilder(genericHID).apply(block) }

    fun control(
        source: HIDSource,
        threshold: Double = HIDButton.DEFAULT_THRESHOLD,
        block: SaturnHIDButtonBuilder.() -> Unit = {}
    ): SaturnHIDButtonBuilder {
        val builder = SaturnHIDButtonBuilder(source, threshold).apply(block)
        controlBuilders.add(builder)
        return builder
    }

    fun build(): SaturnHID<T> {
        val controls = controlBuilders.map { it.build() }
        val stateControls = stateControlBuilders.mapValues { it.value.build() }
        return SaturnHID(genericHID, controls, stateControls)
    }
}

abstract class SaturnHIDControlBuilder(val source: HIDSource) {
    abstract fun build(): HIDControl
}

class SaturnHIDButtonBuilder(source: HIDSource, private val threshold: Double) : SaturnHIDControlBuilder(source) {
    private val whileOff = mutableListOf<HIDControlListener>()
    private val whileOn = mutableListOf<HIDControlListener>()
    private val changeOn = mutableListOf<HIDControlListener>()
    private val changeOff = mutableListOf<HIDControlListener>()

    fun change(command: Command) {
        changeOn(command)
        changeOff(command)
    }

    fun changeOn(command: Command) = changeOn { command.schedule() }
    fun changeOff(command: Command) = changeOff { command.cancel() }

    fun whileOff(block: HIDControlListener) = whileOff.add(block)
    fun whileOn(block: HIDControlListener) = whileOn.add(block)
    fun changeOn(block: HIDControlListener) = changeOn.add(block)
    fun changeOff(block: HIDControlListener) = changeOff.add(block)

    override fun build() = HIDButton(source, threshold, whileOff, whileOn, changeOn, changeOff)
}

class SaturnHID<T : GenericHID>(
    private val genericHID: T,
    private val controls: List<HIDControl>,
    private val stateControls: Map<BooleanSource, SaturnHID<T>>
) {


    fun getRawAxis(axisId: Int): Double = genericHID.getRawAxis(axisId)
    fun getRawButton(buttonId: Int): Boolean = genericHID.getRawButton(buttonId)

    var leftRumble by Delegates.observable(0.0) { _, _, new ->
        genericHID.setRumble(GenericHID.RumbleType.kLeftRumble, new)
    }

    var rightRumble by Delegates.observable(0.0) { _, _, new ->
        genericHID.setRumble(GenericHID.RumbleType.kRightRumble, new)
    }

    fun update() {
        controls.forEach { it.update() }
        stateControls.filterKeys { it() }.values.forEach { it.update() }
    }
}
