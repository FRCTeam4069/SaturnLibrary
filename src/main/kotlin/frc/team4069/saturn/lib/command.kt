package frc.team4069.saturn.lib

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.InstantCommand

inline fun command(crossinline method: () -> Unit): Command = object : InstantCommand() {
    override fun initialize() {
        method()
    }
}
