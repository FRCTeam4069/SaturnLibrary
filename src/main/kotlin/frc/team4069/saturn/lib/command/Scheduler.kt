package frc.team4069.saturn.lib.command

import edu.wpi.first.wpilibj.IterativeRobot
import frc.team4069.saturn.lib.util.containsAny
import java.util.*

/**
 * Runner for commands in the Saturn command system
 */
object Scheduler {
    private val subsystems = mutableSetOf<Subsystem>()
    private val buttonSchedulers = mutableSetOf<Runnable>()
    internal val queuedCommands = LinkedList<Command>()
    internal val suspendedCommands = mutableListOf<Command>()

    /**
     * Called frequently to update the state of all commands
     *
     * Should be used from [IterativeRobot.teleopPeriodic], and [IterativeRobot.autonomousPeriodic]
     */
    fun run() {
        buttonSchedulers.forEach(Runnable::run)
        subsystems.forEach(Subsystem::periodic)

        // Run all the queued commands
        queuedCommands.forEach(Command::periodic)

        // Finish any commands at their threshold
        queuedCommands.filter(Command::isFinished).forEach {
            it.onFinish()
            queuedCommands.remove(it)
        }

        // Resume any commands that were suspended due to conflicts but can be resumed
        suspendedCommands.filterNot { command -> queuedCommands.any { it.requiredSystems.containsAny(command.requiredSystems) } }
                .forEach {
                    suspendedCommands.remove(it)
                    queuedCommands.add(it)
                    it.onResume()
                }

        // Queue default commands for any subsystems not in use
        subsystems.filterNot { subsystem -> queuedCommands.any { it.requiredSystems.contains(subsystem) } }
                .forEach {
                    add(it.defaultCommand ?: return@forEach)
                }
    }

    /**
     * Queues a command to be run, suspending any commands with conflicts. By default the newest command is the one to survive
     */
    fun add(command: Command) = when (command) {
            is CommandGroup -> addGroup(command)
            else -> {
                val conflict = queuedCommands.find { it.requiredSystems.containsAny(command.requiredSystems) }
                if (conflict != null) {
                    queuedCommands.remove(conflict)
                    suspendedCommands.add(conflict)
                    conflict.onSuspend()
                }

                queuedCommands.add(command)
                command.onCreate()
            }
        }

    private fun addGroup(command: CommandGroup) {
        // Schedule all the parallel children to be run independent
        command.parallelChildren.forEach(this::add)

        // Add this command like any other, sequential behaviour implemented inside
        val conflict = queuedCommands.find { it.requiredSystems.containsAny(command.requiredSystems) }
        if (conflict != null) {
            queuedCommands.remove(conflict)
            suspendedCommands.add(conflict)
            conflict.onSuspend()
        }

        queuedCommands.add(command)
        command.onCreate()
    }

    fun cancel(command: Command) {
        if (!queuedCommands.any { it == command }) {
            throw IllegalArgumentException("No such command running")
        }

        queuedCommands.remove(command)
        command.onCancelled()
    }

    fun addButtonScheduler(scheduler: Runnable) {
        buttonSchedulers.add(scheduler)
    }

    fun clear() {
        queuedCommands.forEach(Command::onFinish)
        queuedCommands.clear()
        suspendedCommands.clear()
    }

    internal fun registerSubsystem(subsystem: Subsystem) {
        subsystems.add(subsystem)
    }
}
