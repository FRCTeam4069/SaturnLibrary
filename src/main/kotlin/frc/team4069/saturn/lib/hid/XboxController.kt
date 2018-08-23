package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.XboxController
import frc.team4069.saturn.lib.hid.HIDButton.Companion.DEFAULT_THRESHOLD

fun xboxController(port: Int, block: SaturnHIDBuilder<XboxController>.() -> Unit) = controller(XboxController(port), block)

fun SaturnHIDBuilder<XboxController>.button(button: XboxButton, block: HIDButtonBuilder.() -> Unit = {}) = button(button.value, block = block)
fun SaturnHIDBuilder<XboxController>.triggerAxisButton(hand: GenericHID.Hand, threshold: Double = DEFAULT_THRESHOLD, block: HIDButtonBuilder.() -> Unit = {})
    = axisButton(triggerToRaw(hand), threshold, block)


fun SaturnHID<XboxController>.getY(hand: GenericHID.Hand) = getRawAxis(yAxisToRaw(hand))
fun SaturnHID<XboxController>.getX(hand: GenericHID.Hand) = getRawAxis(xAxisToRaw(hand))
fun SaturnHID<XboxController>.getRawButton(button: XboxButton) = getRawButton(button.value)

private fun yAxisToRaw(hand: GenericHID.Hand) = if(hand == GenericHID.Hand.kLeft) 1 else 5
private fun xAxisToRaw(hand: GenericHID.Hand) = if(hand == GenericHID.Hand.kLeft) 0 else 4
private fun triggerToRaw(hand: GenericHID.Hand) = if(hand == GenericHID.Hand.kLeft) 2 else 3

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
