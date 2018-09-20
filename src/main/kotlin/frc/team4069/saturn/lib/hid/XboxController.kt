package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.XboxController

fun xboxController(port: Int, block: XboxBuilder.() -> Unit): XboxController {
    val controller = XboxController(port)
    XboxBuilder(controller).apply(block)

    return controller
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

class XboxBuilder(private val controller: XboxController) {
    fun button(id: XboxButton, block: HIDButton.() -> Unit) {
        HIDButton(id, controller).apply(block)
    }
}
