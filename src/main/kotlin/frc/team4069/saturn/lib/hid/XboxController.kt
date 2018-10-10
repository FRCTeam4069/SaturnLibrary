package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.XboxController

fun xboxController(port: Int, block: SaturnHIDBuilder<XboxController>.() -> Unit): SaturnHID<XboxController> {
    return SaturnHIDBuilder(XboxController(port)).apply(block).build()
}

val kA = XboxButton(1)
val kB = XboxButton(2)
val kX = XboxButton(3)
val kY = XboxButton(4)
val kBumperLeft = XboxButton(5)
val kBumperRight = XboxButton(6)
val kBack = XboxButton(7)
val kStart = XboxButton(8)
val kLeftStick = XboxButton(9)
val kRightStick = XboxButton(10)

class XboxButton internal constructor(val value: Int)

fun SaturnHIDBuilder<XboxController>.button(button: XboxButton, builder: SaturnHIDButtonBuilder.() -> Unit) =
    button(button.value, block = builder)

fun SaturnHIDBuilder<XboxController>.triggerAxisButton(
    hand: GenericHID.Hand,
    threshold: Double = HIDButton.DEFAULT_THRESHOLD,
    block: SaturnHIDButtonBuilder.() -> Unit = {}
) = axisButton(yTriggerAxisToRawAxis(hand), threshold, block)

// Source Helpers
fun SaturnHID<XboxController>.getY(hand: GenericHID.Hand) = getRawAxis(yAxisToRawAxis(hand))
fun SaturnHID<XboxController>.getX(hand: GenericHID.Hand) = getRawAxis(xAxisToRawAxis(hand))
fun SaturnHID<XboxController>.getTriggerAxis(hand: GenericHID.Hand) = getRawAxis(yTriggerAxisToRawAxis(hand))

private fun yAxisToRawAxis(hand: GenericHID.Hand) = if(hand == GenericHID.Hand.kLeft) 1 else 5
private fun xAxisToRawAxis(hand: GenericHID.Hand) = if(hand == GenericHID.Hand.kLeft) 0 else 4
private fun yTriggerAxisToRawAxis(hand: GenericHID.Hand) = if(hand == GenericHID.Hand.kLeft) 2 else 3