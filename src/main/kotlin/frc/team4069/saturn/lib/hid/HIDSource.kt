package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.saturn.lib.util.Source

class HIDButtonSource(private val hid: GenericHID, private val buttonId: Int) : HIDSource {
    override val value: Double
        get() = if(hid.getRawButton(buttonId)) 1.0 else 0.0

}

class HIDAxisSource(private val hid: GenericHID, private val axisId: Int) : HIDSource {
    override val value: Double
        get() = hid.getRawAxis(axisId)

}

class HIDPOVSource(private val hid: GenericHID, private val povId: Int, private val angle: Int) : HIDSource {
    override val value: Double
        get() = if(hid.getPOV(povId) == angle) 1.0 else 0.0

}

interface HIDSource : Source<Double>
