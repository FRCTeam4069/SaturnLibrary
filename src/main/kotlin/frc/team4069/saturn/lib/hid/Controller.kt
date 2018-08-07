package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.XboxController

class Controller(port: Int) : XboxController(port) {

    fun button(button: ButtonType) = ControllerButton(button.id, this)

    fun button(button: ButtonType, block: ControllerButton.() -> Unit): ControllerButton {
        return ControllerButton(button.id, this).apply(block)
    }
}

inline fun controller(port: Int, block: Controller.() -> Unit): Controller {
    return Controller(port).apply(block)
}


