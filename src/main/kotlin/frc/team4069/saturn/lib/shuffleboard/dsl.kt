package frc.team4069.saturn.lib.shuffleboard

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

fun <T> chooser(name: String, block: SendableChooserDsl<T>.() -> Unit): SendableChooser<T> {
    val chooser = SendableChooserDsl(SendableChooser<T>()).apply(block).finalize()
    SmartDashboard.putData(name, chooser)
    return chooser
}

class SendableChooserDsl<T>(val chooser: SendableChooser<T>) {
    private val values = mutableMapOf<String, T>()
    private var defaultKey: String? = null

    operator fun String.plusAssign(value: T) {
        values[this] = value
    }

    operator fun String.unaryPlus() {
        defaultKey = this
    }

    fun finalize(): SendableChooser<T> {
        if (defaultKey != null) {
            chooser.setDefaultOption(defaultKey!!, values.remove(defaultKey!!)!!)
        }

        values.forEach { (s, v) -> chooser.addOption(s, v) }
        return chooser
    }
}
