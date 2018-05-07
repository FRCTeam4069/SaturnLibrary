package frc.team4069.saturn.lib.command

abstract class ButtonScheduler(protected val command: Command) {
    abstract fun execute()
}