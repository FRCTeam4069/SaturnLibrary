package frc.team4069.saturn.lib.command

import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock

object SubsystemHandler {

    private val subsystemMux = Mutex()
    private val subsystems = mutableListOf<Subsystem>()

    private var started = false

    fun isRegistered(subsystem: Subsystem) = subsystems.contains(subsystem)

    suspend fun addSubsystem(subsystem: Subsystem) = subsystemMux.withLock {
        if(started) {
            throw IllegalStateException("Can't register subsystem when already started")
        }

        subsystems.add(subsystem)
        println("Added ${subsystem::class.java.simpleName}")
    }

    suspend fun startDefaultCommands() = subsystemMux.withLock {
        if(started) {
            throw IllegalStateException("Tried to start default commands twice")
        }

        started = true
        subsystems.mapNotNull(Subsystem::defaultCommand).forEach { it.start() }
    }
}