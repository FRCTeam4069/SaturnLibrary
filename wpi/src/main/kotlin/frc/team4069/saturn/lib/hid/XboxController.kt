/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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