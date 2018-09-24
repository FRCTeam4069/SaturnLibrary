package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.saturn.lib.hid.HIDButton.Companion.DEFAULT_THRESHOLD

fun <T: GenericHID> controller(hid: T, block: SaturnHIDBuilder<T>.() -> Unit): SaturnHID<T> {
    return SaturnHIDBuilder(hid).apply(block).build()
}

class SaturnHIDBuilder<T : GenericHID>(private val inner: T) {
    private val controlBuilders = mutableListOf<SaturnHIDControlBuilder>()

    fun button(buttonId: Int, block: HIDButtonBuilder.() -> Unit) = button(HIDButtonSource(inner, buttonId), block = block)
    fun axisButton(axisId: Int, threshold: Double = DEFAULT_THRESHOLD, block: HIDButtonBuilder.() -> Unit) = button(HIDAxisSource(inner, axisId), threshold, block)

    fun button(src: HIDSource, threshold: Double = DEFAULT_THRESHOLD, block: HIDButtonBuilder.() -> Unit): HIDButtonBuilder {
        val builder = HIDButtonBuilder(src, threshold)
        controlBuilders.add(builder)
        builder.block()
        return builder
    }

    fun build(): SaturnHID<T> {
        val controls = controlBuilders.map { it.build() }
        return SaturnHID(inner, controls)
    }
}

interface SaturnHIDControlBuilder {
    fun build(): HIDControl
}

class HIDButtonBuilder(private val src: HIDSource, private val threshold: Double) : SaturnHIDControlBuilder {
    private val changeOn = mutableListOf<HIDListener>()
    private val changeOff = mutableListOf<HIDListener>()

    fun changeOn(command: Command) = changeOn { command.start() }
    fun changeOff(command: Command) = changeOff { command.start() }

    fun changeOn(block: HIDListener) = changeOn.add(block)
    fun changeOff(block: HIDListener) = changeOff.add(block)

    override fun build() = HIDButton(src, threshold, changeOn, changeOff)
}

class SaturnHID<T : GenericHID>(val inner: T, private val controls: List<HIDControl>) {
    fun getRawAxis(axisId: Int) = HIDAxisSource(inner, axisId)
    fun getRawButton(buttonId: Int) = HIDButtonSource(inner, buttonId)

    suspend fun update() {
        controls.forEach { it.update() }
    }
}