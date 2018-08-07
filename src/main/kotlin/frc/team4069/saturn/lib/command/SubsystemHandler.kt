package frc.team4069.saturn.lib.command

import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock
import java.util.concurrent.CopyOnWriteArrayList

object SubsystemHandler {
    private val subsystemMux = Mutex()
    private val subsystems = CopyOnWriteArrayList<Subsystem>()

    private var alreadyStarted = false

    fun isRegistered(subsystem: Subsystem) = subsystems.contains(subsystem)

    suspend fun addSubsystem(subsystem: Subsystem) = subsystemMux.withLock {
        if(alreadyStarted) throw IllegalStateException("Subsystems cannot be registered after the initialize stage")
        subsystems.add(subsystem)
        println("[Subsystem Handler] Added ${subsystem::class.java.simpleName}")
    }

    suspend fun startDefaultCommands() = subsystemMux.withLock {
        if(alreadyStarted) throw IllegalStateException("Attempted to starte default commands twice")
        alreadyStarted = true

        subsystems.forEach { it.defaultCommand?.start() }
    }
}