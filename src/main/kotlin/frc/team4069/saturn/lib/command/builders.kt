package frc.team4069.saturn.lib.command

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.CommandGroup

inline fun commandGroup(block: CommandGroupBuilder.() -> Unit): CommandGroup {
    return CommandGroupBuilder().apply(block).build()
}

class CommandGroupBuilder {
    private enum class Type {
        SEQUENTIAL,
        PARALLEL
    }

    private val commands = mutableListOf<Pair<Command, Type>>()

    operator fun Command.unaryPlus() = commands.add(this to Type.SEQUENTIAL)
    operator fun Command.unaryMinus() = commands.add(this to Type.PARALLEL)

    fun build() = object : CommandGroup() {
        init {
            commands.forEach { (cmd, ty) ->
                when(ty) {
                    Type.SEQUENTIAL -> addSequential(cmd)
                    Type.PARALLEL -> addParallel(cmd)
                }
            }
        }
    }
}
