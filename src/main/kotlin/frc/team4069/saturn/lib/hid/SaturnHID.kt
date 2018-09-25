package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.saturn.lib.command.Command

fun <T : GenericHID> controller(hid: T, block: SaturnHIDBuilder<T>.() -> Unit): SaturnHID<T> = SaturnHIDBuilder(hid).apply(block).build()

class SaturnHIDBuilder<T : GenericHID>(private val hid: T) {
    private val controls = mutableListOf<HIDControl>()

    fun button(buttonId: Int, block: SaturnHIDButtonBuilder.() -> Unit) {
        val source = HIDButtonSource(hid, buttonId)
        control(source, block = block)
    }

    fun axisButton(axisId: Int, block: SaturnHIDButtonBuilder.() -> Unit) {
        val source = HIDAxisSource(hid, axisId)
        control(source, block = block)
    }

    fun pov(angle: Int, block: SaturnHIDButtonBuilder.() -> Unit) = pov(0, angle, block)
    fun pov(povId: Int, angle: Int, block: SaturnHIDButtonBuilder.() -> Unit) {
        val source = HIDPOVSource(hid, povId, angle)
        control(source, block = block)
    }

    fun control(source: HIDSource, threshold: Double = HIDButton.DEFAULT_THRESHOLD, block: SaturnHIDButtonBuilder.() -> Unit) {
        val control = SaturnHIDButtonBuilder(source, threshold).apply(block).build()
        controls.add(control)
    }

    fun build(): SaturnHID<T> = SaturnHID(hid, controls)
}

class SaturnHID<T : GenericHID>(private val hid: T, private val controls: List<HIDControl>) {
    fun getRawAxis(id: Int) = hid.getRawAxis(id)
    fun getRawButton(id: Int) = hid.getRawButton(id)

    suspend fun update() {
        controls.forEach { it.update() }
    }
}

abstract class SaturnHIDControlBuilder(val source: HIDSource) {
    abstract fun build(): HIDControl
}

class SaturnHIDButtonBuilder(source: HIDSource, private val threshold: Double) : SaturnHIDControlBuilder(source) {

    private val whileOff = mutableListOf<HIDControlListener>()
    private val whileOn = mutableListOf<HIDControlListener>()
    private val changeOff = mutableListOf<HIDControlListener>()
    private val changeOn = mutableListOf<HIDControlListener>()

    fun change(command: Command) {
        changeOn(command)

        changeOff {
            command.stop()
        }
    }

    fun changeOn(command: Command) = changeOn { command.start() }
    fun changeOff(command: Command) = changeOff { command.start() }

    fun whileOff(listener: HIDControlListener) = whileOff.add(listener)
    fun whileOn(listener: HIDControlListener) = whileOn.add(listener)
    fun changeOn(listener: HIDControlListener) = changeOn.add(listener)
    fun changeOff(listener: HIDControlListener) = changeOff.add(listener)

    override fun build(): HIDControl = HIDButton(source, threshold, whileOff, whileOn, changeOn, changeOff)
}