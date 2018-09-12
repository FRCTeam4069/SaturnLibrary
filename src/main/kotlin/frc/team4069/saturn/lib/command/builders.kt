package frc.team4069.saturn.lib.command

fun parallel(block: ParallelGroupBuilder.() -> Unit): ParallelCommandGroup {
    return ParallelGroupBuilder().apply(block).build()
}

fun sequential(block: SequentialGroupBuilder.() -> Unit): SequentialCommandGroup {
    return SequentialGroupBuilder().apply(block).build()
}

class ParallelGroupBuilder {
    val commands = mutableListOf<Command>()

    operator fun Command.unaryPlus() {
        commands += this
    }

    fun parallel(block: ParallelGroupBuilder.() -> Unit): ParallelCommandGroup {
        return ParallelGroupBuilder().apply(block).build()
    }

    fun sequential(block: SequentialGroupBuilder.() -> Unit): SequentialCommandGroup {
        return SequentialGroupBuilder().apply(block).build()
    }

    internal fun build(): ParallelCommandGroup = ParallelCommandGroup(commands)
}

class SequentialGroupBuilder {
    val commands = mutableListOf<Command>()

    operator fun Command.unaryPlus() {
        commands += this
    }

    fun parallel(block: ParallelGroupBuilder.() -> Unit): ParallelCommandGroup {
        return ParallelGroupBuilder().apply(block).build()
    }

    fun sequential(block: SequentialGroupBuilder.() -> Unit): SequentialCommandGroup {
        return SequentialGroupBuilder().apply(block).build()
    }

    internal fun build() = SequentialCommandGroup(commands)
}