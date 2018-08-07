package frc.team4069.saturn.lib.command

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock
import java.util.concurrent.TimeUnit

object CommandHandler {
    private val commandPool = newFixedThreadPoolContext(4, "Command")

    private val subsystemTasks = mutableMapOf<Subsystem, CommandTaskImpl>()
    private val tasks = mutableListOf<CommandTaskImpl>()

    private sealed class CommandEvent {
        data class StartEvent(val command: Command) : CommandEvent()
        data class StopCommandEvent(val command: Command) : CommandEvent()
        data class StopEvent(val command: CommandTask, val startDefault: (Subsystem) -> Boolean) : CommandEvent()
    }

    private val commandActor = actor<CommandEvent>(context = commandPool, capacity = Channel.UNLIMITED) {
        for(evt in channel) {
            handleEvent(evt)
        }
    }

    private suspend fun handleEvent(event: CommandEvent) {
        when(event) {
            is CommandEvent.StartEvent -> {
                val command = event.command
                val subsystems = command.requiredSubsystems

                val commandsToStop = subsystemTasks.filterKeys { subsystems.contains(it) }.values.toSet()
                commandsToStop.forEach { stopCommand ->
                    handleEvent(CommandEvent.StopEvent(stopCommand) {
                        !subsystems.contains(it)
                    })
                }
                val task = CommandTaskImpl(command)
                for(subsystem in subsystems) {
                    subsystemTasks[subsystem] = task
                }

                tasks.add(task)
                task.initialize()
            }
            is CommandEvent.StopCommandEvent -> {
                val task = tasks.find { it.command == event.command } ?: return
                handleEvent(CommandEvent.StopEvent(task) { true })
            }
            is CommandEvent.StopEvent -> {
                val command = event.command
                command.dispose()
                tasks.removeIf { it == command }
                val subsystems = subsystemTasks.filterValues { it == command }.keys

                for(subsystem in subsystems) {
                    if(event.startDefault(subsystem)) {
                        handleEvent(CommandEvent.StartEvent(subsystem.defaultCommand ?: continue))
                    }
                }
            }
        }
    }

    suspend fun start(command: Command) {
        command.requiredSubsystems.filterNot { SubsystemHandler.isRegistered(it) }
                .forEach {
                    throw IllegalStateException("A command required an unregistered subsystem. Command: ${command::class.java.simpleName}. Subsystem: ${it.name} ${it::class.java.name}")
                }
        commandActor.send(CommandEvent.StartEvent(command))
    }

    suspend fun stop(command: Command) = commandActor.send(CommandEvent.StopCommandEvent(command))

    private open class CommandTaskImpl(command: Command) : CommandTask(command) {
        override suspend fun stop() = stop(command)
    }

    abstract class CommandTask(val command: Command) {
        private val commandMux = Mutex()
        private lateinit var updater: Job
        private lateinit var finishHandle: DisposableHandle

        private var isFinished: Boolean? = null
        private var started = false

        suspend fun initialize() = commandMux.withLock {
            started = true
            command.state = Command.State.RUNNING

            finishHandle = command.exposedCondition.invokeOnCompletion {
                stop()
            }
            command.initialize()
            updater = launch(context = commandPool) {
                val frequency = command.updateFrequency
                when {
                    frequency == 0 -> return@launch
                    frequency < 0 -> throw IllegalArgumentException("Command frequency cannot be less than 0")
                }
                val delta = TimeUnit.SECONDS.toNanos(1) / frequency // Time between updates

                var nextNS = System.nanoTime() + delta

                while(isActive) {
                    if(command.isFinished()) {
                        isFinished = true
                        // Stop if we're finished
                        stop()
                        return@launch
                    }
                    commandMux.withLock {
                        if(!isActive) return@launch

                        command.execute() // Tick
                    }
                }

                val delayValue = nextNS - System.nanoTime()
                nextNS += delta
                delay(delayValue, TimeUnit.NANOSECONDS)
            }
        }

        suspend fun dispose() = commandMux.withLock {
            if(!started) return
            val isFinished = this.isFinished ?: command.isFinished()
            updater.cancel()
            command.state = if(isFinished) {
                Command.State.FINISHED
            }else {
                Command.State.CANCELLED
            }

            finishHandle.dispose()
            command.dispose()

            for(listener in command.completionListeners) {
                listener(command)
            }
        }

        abstract suspend fun stop()
    }
}