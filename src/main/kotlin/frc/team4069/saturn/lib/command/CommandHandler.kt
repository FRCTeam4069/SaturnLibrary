package frc.team4069.saturn.lib.command

import frc.team4069.saturn.lib.util.containsAny
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.channels.sendBlocking
import kotlinx.coroutines.experimental.newSingleThreadContext

object CommandHandler {

    private val tasks = mutableListOf<CommandTask>()

    private val context = newSingleThreadContext("Command Handler")

    private sealed class Message {
        data class Start(val command: Command) : Message()
        data class Stop(val command: Command) : Message()
        data class StopTask(val task: CommandTask, val shouldStartDefault: (Subsystem) -> Boolean = { true }) :
            Message()
    }

    private val commandActor = actor<Message>(context, capacity = Channel.UNLIMITED) {
        for (msg in channel) {
            handle(msg)
        }
    }

    private suspend fun handle(msg: Message) {
        when (msg) {
            is Message.Start -> {
                val command = msg.command
                val subsystems = command.requiredSubsystems
                tasks.filter { it.command.requiredSubsystems.containsAny(subsystems) }
                    .forEach {
                        handle(Message.StopTask(it) { subsystem -> subsystem !in subsystems })
                    }

                val task = CommandTask(command, ::stop)
                tasks.add(task)
                task._start()
            }
            is Message.Stop -> {
                val task = tasks.find { it.command == msg.command } ?: return
                handle(Message.StopTask(task))
            }
            is Message.StopTask -> {
                val task = msg.task
                if(task !in tasks) {
                    return
                }

                task._stop()
                tasks -= task
                task.command.requiredSubsystems.asSequence().filter { msg.shouldStartDefault(it) }
                    .mapNotNull(Subsystem::defaultCommand)
                    .forEach {
                        handle(Message.Start(it))
                    }

            }
        }
    }

    suspend fun start(command: Command) {
        if(command.requiredSubsystems.any { !SubsystemHandler.isRegistered(it) }) {
            throw IllegalArgumentException("Trying to start a command with unknown subsystems")
        }
        commandActor.send(Message.Start(command))
    }

    suspend fun stop(command: Command) {
        commandActor.send(Message.Stop(command))
    }

    private fun stop(task: CommandTask) = commandActor.sendBlocking(Message.StopTask(task))
}