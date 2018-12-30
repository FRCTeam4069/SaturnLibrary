package frc.team4069.saturn.lib.commands

import edu.wpi.first.wpilibj.command.Subsystem
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

internal object SubsystemHandler {

    private val subsystems = CopyOnWriteArrayList<SaturnSubsystem>()

    private var alreadyStarted = false

    fun addSubsystem(subsystem: SaturnSubsystem) {
        if (alreadyStarted) throw IllegalStateException("You cannot add a subsystem after the initialize stage")
        subsystems.add(subsystem)
        println("[SaturnSubsystem Handler] Added ${subsystem.javaClass.simpleName}")
    }

    fun lateInit() = subsystems.forEach { it.lateInit() }

    fun autoReset() = subsystems.forEach { it.autoReset() }

    fun teleopReset() = subsystems.forEach { it.teleopReset() }

    // https://www.chiefdelphi.com/forums/showthread.php?t=166814
    fun zeroOutputs() = subsystems.forEach { it.zeroOutputs() }
}

/**
 *  Kotlin Wrapper for [WPI's Subsystem][Subsystem]
 *  @param name the name of the subsystem
 */
abstract class SaturnSubsystem(name: String? = null) {
    companion object {
        private val subsystemId = AtomicLong()
    }

    val name = name ?: "SaturnSubsystem ${subsystemId.incrementAndGet()}"
    private val _wpiSubsystem = SaturnWpiSubsystem()
    /**
     * Wrapped WPI subsystem
     */
    val wpiSubsystem: Subsystem = _wpiSubsystem

    private inner class SaturnWpiSubsystem : Subsystem(name) {
        override fun initDefaultCommand() {
            defaultCommand = this@SaturnSubsystem.defaultCommand.wrappedValue
        }
    }

    /**
     * The default command, this is run when nothing else is currently requiring the subsystem
     */
    @Suppress("LeakingThis")
    var defaultCommand: SaturnCommand = EmptyCommand(this)
        protected set(value) {
            _wpiSubsystem.defaultCommand = value.wrappedValue
            field = value
        }

    /**
     * Called after all subsystems can be initialized
     */
    open fun lateInit() {}

    /**
     * Called when autonomous mode starts
     */
    open fun autoReset() {}

    /**
     * Called when Tele-Operated mode starts
     */
    open fun teleopReset() {}

    /**
     * Called when no mode is enabled
     */
    open fun zeroOutputs() {}
}
