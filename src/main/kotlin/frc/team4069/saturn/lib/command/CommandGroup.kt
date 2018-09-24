package frc.team4069.saturn.lib.command

import frc.team4069.saturn.lib.util.containsAny
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.channels.sendBlocking
import kotlinx.coroutines.experimental.newFixedThreadPoolContext

open class CommandGroup(private val groupType: GroupType,
                        private val commands: List<Command>) : Command(commands.flatMap { it.requiredSubsystems }) {

    protected var parentCommandGroup: CommandGroup? = null
    private lateinit var commandGroupHandler: CommandGroupHandler
//    private val tasksToRun: MutableSet<CommandGroupTask>
    private lateinit var tasksToRun: MutableSet<CommandGroupTask>

    override val isFinished
        get() = tasksToRun.isEmpty()

    init {
        executeFrequency = 0
        commands.filterIsInstance<CommandGroup>()
            .forEach {
                it.parentCommandGroup = this
            }

    }

    override suspend fun initialize() {
        commandGroupHandler = if(parentCommandGroup == null) ParentCommandGroupHandler() else ChildCommandGroupHandler()
        @Suppress("ConvertCallChainIntoSequence")
        tasksToRun = commands.map { CommandGroupTask(it) }.toMutableSet()

        synchronized(tasksToRun) {
            if(groupType == GroupType.PARALLEL) {
                tasksToRun.forEach { commandGroupHandler.startTask(it) }
            }else {
                tasksToRun.firstOrNull()?.let { commandGroupHandler.startTask(it) }
            }
        }
    }

    override suspend fun dispose() {
        (commandGroupHandler as? ParentCommandGroupHandler)?.stop()
    }

    protected inner class CommandGroupTask(command: Command) : CommandTask(command, commandGroupHandler::handleFinish) {
        val group = this@CommandGroup

        override suspend fun stop() {
            synchronized(tasksToRun) {
                tasksToRun.remove(this)
            }

            if(groupType == GroupType.SEQUENTIAL) {
                tasksToRun.firstOrNull()?.let { commandGroupHandler.startTask(it) }
            }
        }
    }

    private interface CommandGroupHandler {
        fun startTask(task: CommandGroupTask)
        fun handleFinish(task: CommandTask)
    }

    private inner class ChildCommandGroupHandler : CommandGroupHandler by parentCommandGroup!!.commandGroupHandler

    private sealed class GroupMessage {
        data class StartTask(val task: CommandGroupTask) : GroupMessage()
        class FinishTask(val task: CommandGroupTask) : GroupMessage()
    }

    private inner class ParentCommandGroupHandler : CommandGroupHandler {

        private val groupActor = actor<GroupMessage>(commandGroupContext, capacity = Channel.UNLIMITED, start = CoroutineStart.LAZY) {
            for(msg in channel) {
                handle(msg)
            }
        }

        private val allActiveTasks = mutableSetOf<CommandGroupTask>()
        private val queuedTasks = mutableSetOf<CommandGroupTask>()

        private val commandTasks = allActiveTasks.asSequence().map(CommandGroupTask::command)
            .filter { it !is CommandGroup }
            .toList()

        private suspend fun handle(msg: GroupMessage) {
            when(msg) {
                is GroupMessage.StartTask -> {
                    val task = msg.task

                    assert(task !in allActiveTasks) { "$task has been started already." }
                    if(groupActor.isClosedForSend) {
                        println("[Command Group] The start of $task was ignored because this group is disposing")
                    }

                    if(task.command !is CommandGroup) {
                        val used = commandTasks.any { it.requiredSubsystems.containsAny(task.command.requiredSubsystems) }

                        if(used) {
                            // Delay this task while whatever is occupying its subsystems runs
                            queuedTasks.add(task)
                            return
                        }
                    }

                    allActiveTasks += task
                    task._start()
                }
                is GroupMessage.FinishTask -> {
                    val task = msg.task
                    assert(task in allActiveTasks) { "Trying to stop a task that isn't running" }
                    allActiveTasks.remove(task)
                    if(task.command is CommandGroup) {
                        for(sub in queuedTasks.filter { it.group == task.command }) {
                            handle(GroupMessage.FinishTask(sub))
                        }
                        for(sub in allActiveTasks.filter { it.group == task.command }) {
                            handle(GroupMessage.FinishTask(sub))
                        }
                    }
                    task._stop()

                    queuedTasks.iterator().forEach { queued ->
                        val used = commandTasks.any { it.requiredSubsystems.containsAny(queued.command.requiredSubsystems) }
                        if(!used) {
                            queuedTasks.remove(queued)
                            handle(GroupMessage.StartTask(queued))
                        }
                    }
                }
            }
        }

        suspend fun stop() {
            assert(!groupActor.isClosedForSend) { "Actor has already been closed" }
            groupActor.close()

        }

        override fun startTask(task: CommandGroupTask) {
            if(groupActor.isClosedForSend) {
                println("[Command Group] Didn't start $task because this group is disposing")
            }
            groupActor.sendBlocking(GroupMessage.StartTask(task))
        }

        override fun handleFinish(task: CommandTask) =
                groupActor.sendBlocking(GroupMessage.FinishTask(task as CommandGroupTask))

    }

    enum class GroupType {
        PARALLEL,
        SEQUENTIAL,
    }

    companion object {
        val commandGroupContext = newFixedThreadPoolContext(2, "Command Group")
    }
}