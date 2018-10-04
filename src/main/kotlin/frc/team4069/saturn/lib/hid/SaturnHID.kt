package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.command.Command

fun <T : GenericHID> controller(genericHID: T, block: SaturnHIDBuilder<T>.() -> Unit): SaturnHID<T> {
    return SaturnHIDBuilder(genericHID).apply(block).build()
}

class SaturnHIDBuilder<T : GenericHID>(private val genericHID: T) {
    private val controlBuilders = mutableListOf<SaturnHIDControlBuilder>()

    fun button(buttonId: Int, block: SaturnHIDButtonBuilder.() -> Unit = {}) = control(HIDButtonSource(genericHID, buttonId), block = block)
    fun axisButton(axisId: Int, threshold: Double = HIDButton.DEFAULT_THRESHOLD, block: SaturnHIDButtonBuilder.() -> Unit = {}) = control(HIDAxisSource(genericHID, axisId), threshold, block)
    fun pov(angle: Int, block: SaturnHIDButtonBuilder.() -> Unit = {}) = pov(0, angle, block)
    fun pov(pov: Int, angle: Int, block: SaturnHIDButtonBuilder.() -> Unit = {}) = control(HIDPOVSource(genericHID, pov, angle), block = block)

    fun control(source: HIDSource, threshold: Double = HIDButton.DEFAULT_THRESHOLD, block: SaturnHIDButtonBuilder.() -> Unit = {}): SaturnHIDButtonBuilder {
        val builder = SaturnHIDButtonBuilder(source, threshold)
        controlBuilders.add(builder)
        block(builder)
        return builder
    }

    fun build(): SaturnHID<T> {
        val controls = controlBuilders.map { it.build() }
        return SaturnHID(genericHID, controls)
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
        changeOff { command.cancel() }
    }

    fun changeOn(command: Command) = changeOn { command.start() }
    fun changeOff(command: Command) = changeOff { command.start() }

    fun whileOff(block: HIDControlListener) = also { whileOff.add(block) }
    fun whileOn(block: HIDControlListener) = also { whileOn.add(block) }
    fun changeOn(block: HIDControlListener) = also { changeOn.add(block) }
    fun changeOff(block: HIDControlListener) = also { changeOff.add(block) }

    override fun build() = HIDButton(source, threshold, whileOff, whileOn, changeOn, changeOff)
}

class SaturnHID<T : GenericHID>(private val genericHID: T,
                                private val controls: List<HIDControl>) {


    fun getRawAxis(axisId: Int): Double = genericHID.getRawAxis(axisId)
    fun getRawButton(buttonId: Int): Boolean = genericHID.getRawButton(buttonId)

    fun update() {
        controls.forEach { it.update() }
    }
}
