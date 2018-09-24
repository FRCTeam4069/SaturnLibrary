package frc.team4069.saturn.lib.command.builtins

import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.CommandGroup

fun sequential(block: CommandGroupBuilder.() -> Unit) =
    CommandGroupBuilder(CommandGroup.GroupType.SEQUENTIAL).apply(block).build()

fun parallel(block: CommandGroupBuilder.() -> Unit) =
    CommandGroupBuilder(CommandGroup.GroupType.PARALLEL).apply(block).build()

class CommandGroupBuilder(val type: CommandGroup.GroupType) {
    private val commands = mutableListOf<Command>()

    fun sequential(block: CommandGroupBuilder.() -> Unit) =
        CommandGroupBuilder(CommandGroup.GroupType.SEQUENTIAL).apply(block).build().also { commands.add(it) }

    fun parallel(block: CommandGroupBuilder.() -> Unit) =
        CommandGroupBuilder(CommandGroup.GroupType.PARALLEL).apply(block).build().also { commands.add(it) }

    operator fun Command.unaryPlus() = commands.add(this)
    fun build() = CommandGroup(type, commands)
}