package frc.team4069.saturn.lib.command

import frc.team4069.saturn.lib.util.removeOrNull
import java.util.*

open class CommandGroup : Command() {
    internal val parallelChildren = mutableListOf<Command>()
    private val sequentialChildren = LinkedList<Command>()
    private var currentCommand: Command? = null

    fun addParallel(child: Command) {
        parallelChildren.add(child)
    }

    fun addSequential(child: Command) {
        sequentialChildren.add(child)
    }

    override fun onCreate() {
        currentCommand = sequentialChildren.removeOrNull()
        currentCommand?.requiredSystems?.forEach(this::requires)
    }

    override fun onCancelled() {
        currentCommand?.onCancelled()
        sequentialChildren.clear()
    }

    override fun onResume() {
        currentCommand?.onResume()
    }

    override fun onSuspend() {
        currentCommand?.onSuspend()
    }

    override fun periodic() {
        if(currentCommand == null) {
            this.requiredSystems.clear()
            currentCommand = sequentialChildren.removeOrNull()
            currentCommand?.requiredSystems?.forEach(this::requires)
        }
        currentCommand?.periodic()

        if(currentCommand?.isFinished == true) {
            currentCommand?.onFinish()
            currentCommand = null
        }
    }

    override val isFinished
        get() = sequentialChildren.isEmpty()
}