@file:Suppress("unused")

package frc.team4069.saturn.lib.commands

import frc.team4069.saturn.lib.util.Source
import edu.wpi.first.wpilibj.command.ConditionalCommand

// External Extension Helpers

/**
 * Creates a command group builder [block] that will run all commands sequentially
 */
fun sequential(block: BasicCommandGroupBuilder.() -> Unit) =
    commandGroup(SaturnCommandGroup.GroupType.SEQUENTIAL, block)

/**
 * Creates a command group builder [block] that will run all commands in parallel
 */
fun parallel(block: BasicCommandGroupBuilder.() -> Unit) =
    commandGroup(SaturnCommandGroup.GroupType.PARALLEL, block)

private fun commandGroup(type: SaturnCommandGroup.GroupType, block: BasicCommandGroupBuilder.() -> Unit) =
    BasicCommandGroupBuilder(type).apply(block).build()

/**
 * Creates a state command group builder [block] that will run commands with the matching [state]
 */
fun <T> stateCommandGroup(state: Source<T>, block: StateCommandGroupBuilder<T>.() -> Unit) =
    StateCommandGroupBuilder(state).apply(block).build()

// Builders

interface CommandGroupBuilder {
    /**
     * Creates the command group
     */
    fun build(): SaturnCommandGroup
}

/**
 * Creates a [Saturn Command Group][SaturnCommandGroup] with the given [type]
 */
class BasicCommandGroupBuilder(private val type: SaturnCommandGroup.GroupType) :
    CommandGroupBuilder {
    private val commands = mutableListOf<SaturnCommand>()

    /**
     * Adds [SaturnCommand] command to the builder [BasicCommandGroupBuilder]
     */
    operator fun SaturnCommand.unaryPlus() = commands.add(this)

    override fun build() = SaturnCommandGroup(type, commands.map { it.wrappedValue })
}

/**
 * Creates a [Saturn State Command Group][SaturnCommandGroup] with the given [state]
 */
class StateCommandGroupBuilder<T>(private val state: Source<T>) :
    CommandGroupBuilder {
    private val stateMap = mutableMapOf<T, SaturnCommand>()

    /**
     * Run [block] when the state matches any [states]
     */
    fun state(vararg states: T, block: () -> SaturnCommand) = states(states, block())

    /**
     * Run [command] when the state matches any [states]
     */
    fun state(vararg states: T, command: SaturnCommand) = states(states, command)

    /**
     * Run [command] when the state matches any [states]
     */
    fun states(states: Array<out T>, command: SaturnCommand) = states.forEach { state(it, command) }

    /**
     * Run [block] when the state is [state]
     */
    fun state(state: T, block: () -> SaturnCommand) = state(state, block())

    /**
     * Run [command] when the state is [state]
     */
    fun state(state: T, command: SaturnCommand) {
        if (stateMap.containsKey(state)) println("[StateCommandGroup] Warning: state $state was overwritten during building")
        stateMap[state] = command
    }

    override fun build() =
        SaturnCommandGroup(SaturnCommandGroup.GroupType.SEQUENTIAL,
            stateMap.entries.map { (key, command) ->
                object : ConditionalCommand(command.wrappedValue) {
                    override fun condition() = state() == key
                }
            })
}