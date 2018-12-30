package frc.team4069.saturn.lib.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.second
import frc.team4069.saturn.lib.util.BooleanSource
import frc.team4069.saturn.lib.util.Source
import kotlin.properties.Delegates.observable


/**
 * Command will end the moment it begins
 */
abstract class InstantCommand : SaturnCommand() {
    init {
        executeFrequency = 0
        finishCondition += { true }
    }
}

/**
 * Runs [runnable] once and ends the command when its done
 */
class InstantRunnableCommand(private val runnable: suspend () -> Unit) : InstantCommand() {
    override suspend fun initialize() = runnable()
}

/**
 * Calls [runnable] with a given frequency until the exit condition is true
 * @param exitCondition command will end when this is true
 * @param runnableFrequency command will run at the this frequency
 */
class PeriodicRunnableCommand(
    private val runnable: suspend () -> Unit,
    exitCondition: BooleanSource,
    runnableFrequency: Int = -1
) : SaturnCommand() {
    init {
        this.executeFrequency = runnableFrequency
        finishCondition += exitCondition
    }

    override suspend fun execute() = runnable()
}

/**
 * Waits until a condition is true
 * @param condition command will end when this is true
 */
class ConditionCommand(
    condition: BooleanSource
) : SaturnCommand() {
    init {
        executeFrequency = 0
        finishCondition += condition
    }
}

/**
 * Waits for a given amount of time
 * @param delaySource the time to wait
 */
class DelayCommand(private val delaySource: Source<Time>) : SaturnCommand() {

    /**
     * Waits for a given amount of time
     * @param delay the time to wait
     */
    constructor(delay: Time) : this(Source(delay))

    init {
        executeFrequency = 0
    }

    override suspend fun initialize() {
        withTimeout(delaySource())
    }
}

/**
 * Empty as empty can be
 */
class EmptyCommand(vararg requiredSubsystems: SaturnSubsystem) : SaturnCommand(requiredSubsystems.asIterable()) {
    init {
        executeFrequency = 0
    }
}

/**
 * Runs one of two commands based on the current [condition]
 * @param onTrue ran when [condition] is true
 * @param onFalse ran when [condition] is false
 */
class ConditionalCommand(
    val condition: BooleanSource,
    val onTrue: SaturnCommand?,
    val onFalse: SaturnCommand? = null
) : SaturnCommand() {

    override val wrappedValue: Command = WpiConditionalCommand()

    private inner class WpiConditionalCommand : edu.wpi.first.wpilibj.command.ConditionalCommand(
        onTrue?.wrappedValue,
        onFalse?.wrappedValue
    ), IWpiCommand {
        override var timeout by observable(0.second) { _, _, newValue ->
            setTimeout(newValue.second)
        }

        override fun condition(): Boolean = condition.invoke()

        override fun isFinished() = super.isTimedOut() || super.isFinished() || finishCondition()
    }
}