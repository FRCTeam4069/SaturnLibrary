package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.Scheduler

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

    fun changeOn(command: Command) = changeOn { Scheduler.getInstance().add(command) }
    fun changeOff(command: Command) = changeOff { command.start() }

    fun whileOff(block: HIDControlListener) = whileOff.add(block)
    fun whileOn(block: HIDControlListener) = whileOn.add(block)
    fun changeOn(block: HIDControlListener) = changeOn.add(block)
    fun changeOff(block: HIDControlListener) = changeOff.add(block)

    override fun build(): HIDControl {
        println("changeOn: $changeOn. Source $source")
        return HIDButton(source, threshold, whileOff, whileOn, changeOn, changeOff)
    }
}

class SaturnHID<T : GenericHID>(
    private val genericHID: T,
    private val controls: List<HIDControl>
) {


    fun getRawAxis(axisId: Int): Double = genericHID.getRawAxis(axisId)
    fun getRawButton(buttonId: Int): Boolean = genericHID.getRawButton(buttonId)

    fun update() {
        controls.forEach { it.update() }
    }
}
