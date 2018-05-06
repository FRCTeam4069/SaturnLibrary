package frc.team4069.saturn.lib.command

abstract class ButtonScheduler(private val command: Command) {
    abstract fun execute()
}