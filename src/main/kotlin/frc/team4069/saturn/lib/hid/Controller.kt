package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.XboxController

class Controller(port: Int) : XboxController(port) {

    fun button(button: ButtonType) = ControllerButton(button.id, this)
}


