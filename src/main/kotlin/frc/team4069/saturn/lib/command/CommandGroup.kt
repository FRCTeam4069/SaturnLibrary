package frc.team4069.saturn.lib.command

import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock

abstract class CommandGroup(commands: List<Command>) : Command() {
    companion object {
        private val COMMAND_GROUP_CTX = newFixedThreadPoolContext(2, "Command Group")
    }

    protected val commands = commands.map { GroupCommandTask(it) }
    override val requiredSubsystems = commands.map { it.requiredSubsystems }.flatten().toMutableList()
    private val groupCondition = GroupCondition()

    private val actorMux = Mutex()
    private var started = false
    protected val activeCommands = mutableListOf<GroupCommandTask>()

    private sealed class Event {
        object StartEvent : Event()
        class FinishEvent(val task: GroupCommandTask) : Event()
    }

    private val groupActor = actor<Event>(context = COMMAND_GROUP_CTX,
                                          capacity = Channel.UNLIMITED,
                                          start = CoroutineStart.LAZY) {
        actorMux.withLock {
            for(event in channel) {
                handleEvent(event)
            }
        }
    }

    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.StartEvent -> {
                started = true

                if (commands.isEmpty()) {
                    groupCondition.invoke()
                } else {
                    handleStartEvent()
                }
            }
            is Event.FinishEvent -> {
                val task = event.task

                if (!activeCommands.contains(task)) return
                task.dispose()
                activeCommands.remove(task)

                if(activeCommands.isEmpty()) {
                    groupCondition.invoke()
                    return
                }
                handleFinishEvent()
            }
        }
    }

    protected abstract suspend fun handleStartEvent()
    protected open suspend fun handleFinishEvent() {}

    override suspend fun initialize() {
        groupActor.send(Event.StartEvent)
    }

    override suspend fun dispose() {
        groupActor.close()

        actorMux.withLock {
            for(activeCommand in activeCommands) {
                activeCommand.dispose()
            }

            activeCommands.clear()
        }
    }

    protected inner class GroupCommandTask(command: Command) : CommandHandler.CommandTask(command) {
        override suspend fun stop() = groupActor.send(Event.FinishEvent(this))
    }

    private inner class GroupCondition : Condition() {
        suspend operator fun invoke() = invokeCompletionListeners()
        override suspend fun isMet() = started && activeCommands.isEmpty()
    }
}

open class ParallelCommandGroup(commands: List<Command>) : CommandGroup(commands) {
    override suspend fun handleStartEvent() {
        activeCommands += commands

        // Start them all in parallel
        for(activeCommand in activeCommands) {
            activeCommand.initialize()
        }
    }
}

open class SequentialCommandGroup(commands: List<Command>) : CommandGroup(commands) {
    override suspend fun handleStartEvent() {
        activeCommands += commands
        next()
    }

    override suspend fun handleFinishEvent() = next()

    /**
     * Starts the next command at the head of [activeCommands]
     * Superclass handles removing the expired command from the list
     */
    private suspend fun next() = activeCommands.first().initialize()
}