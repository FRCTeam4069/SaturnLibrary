package frc.team4069.saturn.lib.command

import edu.wpi.first.wpilibj.IterativeRobot
import frc.team4069.saturn.lib.util.containsAny
import java.util.*

/**
 * Runner for commands in the Saturn command system
 */
object Scheduler {
    private val subsystems = mutableSetOf<Subsystem>()
    private val queuedCommands = LinkedList<Command>()
    private val suspendedCommands = mutableListOf<Command>()

    /**
     * Called frequently to update the state of all commands
     *
     * Should be used from [IterativeRobot.teleopPeriodic], and [IterativeRobot.autonomousPeriodic]
     */
    fun run() {
        subsystems.forEach(Subsystem::periodic)

        // Resume any commands that were suspended due to conflicts but can be resumed
        for(suspendedCommand in suspendedCommands.iterator()) {
            if(!queuedCommands.any { it.requiredSystems.containsAny(suspendedCommand.requiredSystems) }) {
                suspendedCommands.remove(suspendedCommand)
                queuedCommands.add(suspendedCommand)
                suspendedCommand.resumed()
            }
        }

        // Queue default commands for any subsystems not in use
        subsystems.filterNot { subsystem -> queuedCommands.any { it.requiredSystems.contains(subsystem) } }
                .forEach { add(it.defaultCommand) }

        // Run all the queued commands
        queuedCommands.forEach(Command::periodic)

        // Finish any commands at their threshold
        queuedCommands.filter(Command::isFinished).forEach {
            it.finished()
            queuedCommands.remove(it)
        }
    }

    /**
     * Queues a command to be run, suspending any commands with conflicts. By default the newest command is the one to survive
     */
    fun add(command: Command) {
        val conflict = queuedCommands.find { it.requiredSystems.containsAny(command.requiredSystems) }
        if(conflict != null) {
            queuedCommands.remove(conflict)
            suspendedCommands.add(conflict)
            conflict.suspended()
        }

        queuedCommands.add(command)
        command.initialize()
    }

    internal fun registerSubsystem(subsystem: Subsystem) {
        subsystems.add(subsystem)
    }
}