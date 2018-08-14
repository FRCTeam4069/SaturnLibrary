package frc.team4069.saturn.lib.command.builders

import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.ParallelCommandGroup
import frc.team4069.saturn.lib.command.SequentialCommandGroup

inline fun parallel(block: CommandGroupBuilder.() -> Unit) = commandGroup(CommandGroupBuilder.Type.PARALLEL, block)
inline fun sequential(block: CommandGroupBuilder.() -> Unit) = commandGroup(CommandGroupBuilder.Type.SEQUENTIAL, block)

inline fun commandGroup(type: CommandGroupBuilder.Type, block: CommandGroupBuilder.() -> Unit) = CommandGroupBuilder(type).apply(block).build()

class CommandGroupBuilder(private val type: CommandGroupBuilder.Type) {
    enum class Type {
        PARALLEL,
        SEQUENTIAL
    }

    private val commands = mutableListOf<Command>()

    fun parallel(block: CommandGroupBuilder.() -> Unit) = commandGroup(Type.PARALLEL, block).also { +it }
    fun sequential(block: CommandGroupBuilder.() -> Unit) = commandGroup(Type.SEQUENTIAL, block).also { +it }

    operator fun Command.unaryPlus() = commands.add(this)

    fun build() = when(type) {
        Type.PARALLEL -> ParallelCommandGroup(commands.toList())
        Type.SEQUENTIAL -> SequentialCommandGroup(commands.toList())
    }
}
