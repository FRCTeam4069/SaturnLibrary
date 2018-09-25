package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.XboxController

fun xboxController(port: Int, block: SaturnHIDBuilder<XboxController>.() -> Unit) =
    controller(XboxController(port), block)

fun SaturnHIDBuilder<XboxController>.button(button: XboxButton, block: SaturnHIDButtonBuilder.() -> Unit) =
    button(button.value, block)

fun SaturnHIDBuilder<XboxController>.triggerAxisButton(
    hand: GenericHID.Hand,
    block: SaturnHIDButtonBuilder.() -> Unit
) = axisButton(handToRawTrigger(hand), block)

fun SaturnHID<XboxController>.getX(hand: GenericHID.Hand) = getRawAxis(handToRawX(hand))
fun SaturnHID<XboxController>.getY(hand: GenericHID.Hand) = getRawAxis(handToRawY(hand))
fun SaturnHID<XboxController>.getTriggerAxis(hand: GenericHID.Hand) = getRawAxis(handToRawTrigger(hand))

fun handToRawTrigger(hand: GenericHID.Hand) = if (hand == GenericHID.Hand.kLeft) 2 else 3
fun handToRawX(hand: GenericHID.Hand) = if(hand == GenericHID.Hand.kLeft) 0 else 4
fun handToRawY(hand: GenericHID.Hand) = if(hand == GenericHID.Hand.kLeft) 1 else 5


val kBumperLeft = XboxButton(5)
val kBumperRight = XboxButton(6)
val kStickLeft = XboxButton(9)
val kStickRight = XboxButton(10)
val kA = XboxButton(1)
val kB = XboxButton(2)
val kX = XboxButton(3)
val kY = XboxButton(4)
val kBack = XboxButton(7)
val kStart = XboxButton(8)


class XboxButton internal constructor(val value: Int)
