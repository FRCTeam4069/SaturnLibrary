package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.meter
import kotlin.math.abs

data class MotionProfileGoal(val pos: Length, val maxAbsVel: LinearVelocity,
                             var completionBehaviour: CompletionBehaviour = CompletionBehaviour.OVERSHOOT,
                             val posTolerance: Double = 1E-3, val velTolerance: Double = 1E-2) {

    val flipped: MotionProfileGoal
        get() = MotionProfileGoal(-pos, maxAbsVel, completionBehaviour, posTolerance, velTolerance)

    fun atGoalState(state: MotionState): Boolean {
        return atGoalPos(state.pos) && state.vel.absoluteValue < (maxAbsVel+ velTolerance.meter.velocity)
                ||completionBehaviour == CompletionBehaviour.VIOLATE_MAX_ABS_VEL
    }

    fun atGoalPos(pos: Length): Boolean {
        return abs(pos.meter - this.pos.meter) < posTolerance
    }

    internal fun sanityCheck() {
        if(maxAbsVel.value > velTolerance && completionBehaviour == CompletionBehaviour.OVERSHOOT) {
            completionBehaviour = CompletionBehaviour.VIOLATE_MAX_ACCEL
        }
    }

    enum class CompletionBehaviour {
        OVERSHOOT,
        VIOLATE_MAX_ACCEL,
        VIOLATE_MAX_ABS_VEL
    }
}