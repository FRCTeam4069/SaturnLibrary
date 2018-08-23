package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.saturn.lib.util.BooleanSource
import frc.team4069.saturn.lib.util.Source

typealias HIDSource = Source<Double>

class HIDButtonSource(private val dev: GenericHID, private val buttonId: Int) : HIDSource {
    private val source = object : BooleanSource {
        override val value: Boolean
            get() {
                val value = dev.getRawButton(buttonId)
                return value
            }
    }

    override val value
        get() = if (source.value) 1.0 else 0.0
}

class HIDAxisSource(private val dev: GenericHID, private val axis: Int) : HIDSource {
    override val value
        get() = dev.getRawAxis(axis)
}