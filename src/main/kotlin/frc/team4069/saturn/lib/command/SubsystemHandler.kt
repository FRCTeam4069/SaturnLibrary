package frc.team4069.saturn.lib.command

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext

object SubsystemHandler {
    val SUBSYSTEM_CTX = newFixedThreadPoolContext(2, "Subsystem ticker")

    private val subsystems = mutableListOf<Subsystem>()
    private val handles = mutableMapOf<Subsystem, Job>()
    private var started = false

    sealed class Message {
        /**
         * Message to register a new subsystem
         */
        data class Register(val subsystem: Subsystem) : Message()

        /**
         * Message to start default commands, and start [Subsystem.periodic] ticking
         */
        object Start : Message()
    }

    val subsystemActor = actor<Message> {
        while(isActive) {
            for(msg in channel) {
                when(msg) {
                    is Message.Register -> registerSubsystem(msg.subsystem)
                    is Message.Start -> start()
                }
            }
        }
    }

    private fun registerSubsystem(subsystem: Subsystem) {
        if(subsystem in subsystems) {
            println("E: Trying to register a subsystem $subsystem that has already been registered")
        }

        if(started) {
            println("E: Trying to register a subsystem after start")
        }

        subsystems += subsystem
    }

    private suspend fun start() {
        if(started) {
            throw RuntimeException("Trying to start subsystem ticker when it's already started")
        }

        println("Starting loopers and default commands")

        started = true
        subsystems.mapNotNull(Subsystem::defaultCommand)
                .forEach {
                    println("Started $it")
                    it.start()
                }

        subsystems.forEach {
            handles[it] = launch(context = SUBSYSTEM_CTX) {
                val delayTime = 1000 / it.updateFrequency

                while(isActive) {
                    it.periodic()
                    delay(delayTime)
                }
            }
        }
    }
}