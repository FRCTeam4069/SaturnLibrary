package frc.team4069.saturn.lib.command

/**
 * The type of group
 * [SEQUENTIAL] groups run commands individually, when one command finishes, the next in the queue starts
 * Order matters.
 *
 * Sequential command groups will terminate once there are no more commands to run
 *
 * [PARALLEL] groups run commands in parallel, with the state of them completely independant from the others
 * Order does not matter
 *
 * Parallel command groups will terminate once all of their children commands have terminated
 */
enum class GroupType {
    SEQUENTIAL,
    PARALLEL
}

open class CommandGroup(val groupType: GroupType, val commands: MutableList<Command>) : Command(*commands.flatMap { it.requiredSubsystems.toList() }.toTypedArray()) {
    val queuedCommands= mutableListOf<Command>()

    override suspend fun initialize() {
        when(groupType) {
            GroupType.PARALLEL -> {
                queuedCommands += commands
                commands.forEach {
                    val handle = it.start().await()
                    handle.invokeOnCompletion {
                        queuedCommands.remove(it)
                    }
                }
            }
            GroupType.SEQUENTIAL -> {
                startNextCommand()
            }
        }
    }

    private suspend fun startNextCommand() {
        queuedCommands.firstOrNull()?.let {
            val handle = it.start().await()
            handle.invokeOnCompletion {
                queuedCommands.removeAt(0)
                startNextCommand()
            }
        }
    }

    override val isFinished
        get() = queuedCommands.isEmpty()
}

class SequentialCommandGroup(commands: List<Command>) : CommandGroup(GroupType.SEQUENTIAL, commands.toMutableList())
class ParallelCommandGroup(commands: List<Command>) : CommandGroup(GroupType.PARALLEL, commands.toMutableList())
