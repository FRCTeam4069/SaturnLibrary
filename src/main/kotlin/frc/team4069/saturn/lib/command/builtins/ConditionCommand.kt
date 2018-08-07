package frc.team4069.saturn.lib.command.builtins

import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.Condition

open class ConditionCommand(condition: Condition) : Command() {
    init {
        finishCondition += condition
    }
}
