package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.saturn.lib.util.Source

typealias HIDSource = Source<Double>

class HIDButtonSource<T : GenericHID>(val device: T, val buttonId: Int) : HIDSource {
    override fun invoke() = if (device.getRawButton(buttonId)) 1.0 else 0.0
}

class HIDAxisSource<T : GenericHID>(val device: T, val axisId: Int) : HIDSource {
    override fun invoke() = device.getRawAxis(axisId)
}

class HIDPOVSource<T : GenericHID>(val device: T, val povId: Int, val angle: Int) : HIDSource {
    override fun invoke() = if (device.getPOV(povId) == angle) 1.0 else 0.0
}