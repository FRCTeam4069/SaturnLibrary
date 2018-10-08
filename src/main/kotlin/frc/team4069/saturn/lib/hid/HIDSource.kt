package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.saturn.lib.util.Source

typealias HIDSource = Source<Double>

class HIDButtonSource<T: GenericHID>(val device: T, val buttonId: Int) : HIDSource {
    override val value: Double
        get() = if(device.getRawButton(buttonId)) 1.0 else 0.0
}

class HIDAxisSource<T: GenericHID>(val device: T, val axisId: Int) : HIDSource {
    override val value: Double
        get() = device.getRawAxis(axisId)
}

class HIDPOVSource<T: GenericHID>(val device: T, val povId: Int, val angle: Int) : HIDSource {
    override val value: Double
        get() = if(device.getPOV(povId) == angle) 1.0 else 0.0
}