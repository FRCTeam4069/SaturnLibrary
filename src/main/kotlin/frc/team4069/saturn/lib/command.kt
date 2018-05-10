package frc.team4069.saturn.lib

import frc.team4069.saturn.lib.command.Command


inline fun command(crossinline method: () -> Unit): Command = object : Command() {

    override fun onCreate() {
        method()
    }

    override val isFinished = true
}
