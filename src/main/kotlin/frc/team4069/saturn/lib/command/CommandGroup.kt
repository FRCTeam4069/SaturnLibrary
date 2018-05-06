package frc.team4069.saturn.lib.command

abstract class CommandGroup {
    internal val parallelChildren = mutableListOf<Command>()
    internal val sequentialChildren = mutableListOf<Command>()

    fun addParallel(child: Command) {
        parallelChildren.add(child)
    }

    fun addSequential(child: Command) {
        TODO()
    }
}