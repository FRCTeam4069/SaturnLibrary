package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.derivedunits.acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.meter
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

class SetpointGenerator {
    var constraints: MotionProfileConstraints? = null
    var goal: MotionProfileGoal? = null
    var profile: MotionProfile? = null

    fun reset() {
        constraints = null
        goal = null
        profile = null
    }

    fun getSetpoint(constraints: MotionProfileConstraints,
                    goal: MotionProfileGoal,
                    prevState: MotionState,
                    t: Time): Setpoint {
        var regenerate = this.constraints == null || constraints != this.constraints ||
                this.goal == null || this.goal != goal || profile == null

        if(!regenerate && !profile!!.isEmpty()) {
            val expectedState = profile!!.stateByTime(prevState.t)
            regenerate = expectedState == null || expectedState != prevState
        }

        if(regenerate) {
            this.constraints = constraints
            this.goal = goal
            this.profile = MotionProfileGenerator.generateProfile(constraints.maxVel, constraints.maxAccel, prevState, goal)
        }

        var rv: Setpoint? = null
        if(!profile!!.isEmpty() && profile!!.isValid()) {
            var setpoint = when {
                t > profile!!.endTime -> profile!!.endState
                t < profile!!.startTime -> profile!!.startState
                else -> profile!!.stateByTime(t)!!
            }
            this.profile!!.trimBeforeTime(t)
            rv = Setpoint(setpoint, profile!!.isEmpty() || goal.atGoalState(setpoint))
        }

        if(rv == null) {
            rv = Setpoint(prevState, true)
        }

        if(rv.finalSetpoint) {
            rv.state = MotionState(rv.state.t, goal.pos,
                    (sign(rv.state.vel.value) * max(goal.maxAbsVel.value, rv.state.vel.value.absoluteValue)).meter.velocity,
                    0.meter.acceleration)
        }

        return rv
    }

    data class Setpoint(var state: MotionState, var finalSetpoint: Boolean)
}