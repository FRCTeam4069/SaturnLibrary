package frc.team4069.saturn.lib.pneumatics

import edu.wpi.first.wpilibj.Solenoid

class Solenoid(pcmId: Int = 0, port: Int) : Solenoid(pcmId, port) {
    var output: Boolean
        get() = this.get()
        set(value) = this.set(value)

}