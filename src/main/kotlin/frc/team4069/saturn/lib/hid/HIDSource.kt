package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.saturn.lib.util.Source

/**
 * Represents a HID source for a binary button.
 * When the button is pressed, [value] will return 1. Else it will return 0
 */
class HIDButtonSource(private val hid: GenericHID, private val buttonId: Int) : HIDSource {
    override val value: Double
        get() = if(hid.getRawButton(buttonId)) 1.0 else 0.0

}

/**
 * Represents a HID source for an analog axis with values ranging from -1.0..1.0 (inclusive).
 */
class HIDAxisSource(private val hid: GenericHID, private val axisId: Int) : HIDSource {
    override val value: Double
        get() = hid.getRawAxis(axisId)

}

/**
 * Represents a HID source for a POV input. [value] will returned 1.0 if the current value of the POV at
 * the time of query is equal to the expected [angle]
 */
class HIDPOVSource(private val hid: GenericHID, private val povId: Int, private val angle: Int) : HIDSource {
    override val value: Double
        get() = if(hid.getPOV(povId) == angle) 1.0 else 0.0

}

interface HIDSource : Source<Double>
