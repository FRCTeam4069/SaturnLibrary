/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package frc.team4069.saturn.lib.commands

import edu.wpi.first.wpilibj2.command.*
import java.lang.IllegalStateException

class CommandGroupBuilder(val type: Type) {
    val commands = mutableListOf<Command>()

    operator fun Command.unaryPlus() {
        commands += this
    }

    fun build() = when(type) {
        Type.SEQUENTIAL -> SequentialCommandGroup(*commands.toTypedArray())
        Type.PARALLEL -> ParallelCommandGroup(*commands.toTypedArray())
        Type.PARALLEL_RACE -> ParallelRaceGroup(*commands.toTypedArray())
    }

    enum class Type {
        SEQUENTIAL,
        PARALLEL,
        PARALLEL_RACE
    }
}

class ParallelDeadlineBuilder {
    var deadline: Command? = null
    val commands = mutableListOf<Command>()

    operator fun Command.unaryMinus() {
        deadline = this
    }

    operator fun Command.unaryPlus() {
        commands += this
    }

    fun build() = if(deadline == null) {
        throw IllegalStateException("Deadline must be nonnull")
    } else {
        ParallelDeadlineGroup(deadline, *commands.toTypedArray())
    }
}

/**
 * Simple DSL for constructing a parallel command group
 *
 * Commands are added with unary `+`, and will all run in parallel when the command is started,
 * the group will be active until all commands have finished.
 *
 * ```
 * val group = parallel {
 *     +Command1()
 *     +Command2()
 *     +Command3()
 * }
 *
 * group.schedule()
 * ```
 */
inline fun parallel(block: CommandGroupBuilder.() -> Unit): CommandGroupBase {
    return CommandGroupBuilder(CommandGroupBuilder.Type.PARALLEL).apply(block).build()
}

/**
 * Simple DSL for constructing a sequential command group
 *
 * Commands are added with unary `+`, and when the group is started, it will run each of the commands
 * sequentially**in order**.
 *
 * Execution is finished when the last command added is complete.
 *
 * ```
 * val group = sequential {
 *     +Command1()
 *     +Command2()
 *     +Command3()
 * }
 * ```
 */
inline fun sequential(block: CommandGroupBuilder.() -> Unit): CommandGroupBase {
    return CommandGroupBuilder(CommandGroupBuilder.Type.SEQUENTIAL).apply(block).build()
}

/**
 * A simple DSL for creating a parallel race group.
 *
 * Commands are added with unary `+`, when the group is started, all the commands are started in parallel.
 * Once one command in the command group finishes, the rest are interrupted and the group stops executing.
 *
 * ```
 * val group = parallelRace {
 *     +Command1()
 *     +Command2()
 *     +Command3()
 * }
 * ```
 */
inline fun parallelRace(block: CommandGroupBuilder.() -> Unit): CommandGroupBase {
    return CommandGroupBuilder(CommandGroupBuilder.Type.PARALLEL_RACE).apply(block).build()
}

/**
 * A simple DSL for creating a parallel deadline group.
 *
 * The deadline command is specified with unary `-`, while other commands are added with unary `+`.
 *
 * When started, all of the commands are started in parallel. When the deadline command is finished,
 * all other commands are interrupted and the group finishes executing.
 *
 * ```
 * val group = parallelDeadline {
 *     -DeadlineCommand()
 *     +Command1()
 *     +Command2()
 *     +Command3()
 * }
 * ```
 */
inline fun parallelDeadline(block: ParallelDeadlineBuilder.() -> Unit): ParallelDeadlineGroup {
    return ParallelDeadlineBuilder().apply(block).build()
}
