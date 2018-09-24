package frc.team4069.saturn.lib.command

import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock

object SubsystemHandler {
    private val subsystemMux = Mutex()
    private val subsystems = mutableListOf<Subsystem>()

    private var alreadyStarted = false

    fun isRegistered(subsystem: Subsystem) = subsystem in subsystems

    suspend fun addSubsystem(subsystem: Subsystem) = subsystemMux.withLock {
        if(alreadyStarted) throw IllegalStateException("Cannot register a subsystem after initialization")
        subsystems.add(subsystem)
        println("Added ${subsystem::class.java.simpleName}")
    }

    suspend fun startDefaultCommands() = subsystemMux.withLock {
        if(alreadyStarted) throw IllegalStateException("Cannot start default commands after initialization")
        alreadyStarted = true

        subsystems.mapNotNull(Subsystem::defaultCommand).forEach { it.start() }
    }
}