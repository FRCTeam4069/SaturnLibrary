package frc.team4069.saturn.lib.command

open class InstantCommand(vararg val subsystems: Subsystem) : Command(*subsystems) {
    override val isFinished = true
}
