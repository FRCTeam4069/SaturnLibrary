package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.meter
import frc.team4069.saturn.lib.mathematics.units.second
import kotlin.math.*

object MotionProfileGenerator {

    private fun generateFlippedProfile(constraints: MotionProfileConstraints,
                                       startState: MotionState,
                                       endState: MotionProfileGoal): MotionProfile {
        val profile = generateProfile(constraints.maxVel, constraints.maxAccel, startState.flipped, endState.flipped)

        for(s in profile.segments) {
            s.start = s.start.flipped
            s.end = s.end.flipped
        }

        return profile
    }

    fun generateProfile(maxVel: LinearVelocity, maxAccel: LinearAcceleration,
                        startState: MotionState, endState: MotionProfileGoal): MotionProfile {
        var deltaPos = endState.pos - startState.pos
        val constraints = MotionProfileConstraints(maxVel, maxAccel)
        if(deltaPos.meter < 0.0 || (deltaPos.meter == 0.0 && startState.vel.value < 0.0)) {
            return generateFlippedProfile(constraints, startState, endState)
        }

        val vel = startState.vel.value
        val acc = startState.acc.value
        var startState = MotionState(startState.t, startState.pos,
                (sign(vel) * min(vel.absoluteValue, constraints.maxVel.value)).meter.velocity,
                (sign(acc) * min(acc.absoluteValue, constraints.maxAccel.value)).meter.acceleration)
        val profile = MotionProfile()
        profile.reset(startState)

        if(startState.vel.value < 0.0 && deltaPos.meter > 0.0) {
            val stoppingTime = abs(startState.vel.value / constraints.maxAccel.value)
            profile.appendControl(constraints.maxAccel, stoppingTime.second)
            startState = profile.endState
            deltaPos = endState.pos - startState.pos
        }

        val minAbsVelAtGoalSqr = startState.vel.value.pow(2) - 2.0 * constraints.maxAccel.value * deltaPos.meter
        val minAbsVelAtGoal = sqrt(minAbsVelAtGoalSqr.absoluteValue)
        val maxAbsVelAtGoal = sqrt(startState.vel.value.pow(2) + 2.0 * constraints.maxAccel.value * deltaPos.meter)
        var goalVel = endState.maxAbsVel.value
        var maxAcc = constraints.maxAccel.value
        if(minAbsVelAtGoalSqr > 0.0
                && minAbsVelAtGoal > (endState.maxAbsVel.value + endState.velTolerance)) {
            if(endState.completionBehaviour == MotionProfileGoal.CompletionBehaviour.VIOLATE_MAX_ABS_VEL) {
                goalVel = minAbsVelAtGoal
            }else if(endState.completionBehaviour == MotionProfileGoal.CompletionBehaviour.VIOLATE_MAX_ACCEL) {
                if(deltaPos.meter.absoluteValue < endState.posTolerance) {
                    profile.appendSegment(
                            ProfileSegment(
                                    MotionState(profile.endTime, profile.endPos, profile.endState.vel,
                                            Double.NEGATIVE_INFINITY.meter.acceleration),
                                    MotionState(profile.endTime, profile.endPos, goalVel.meter.velocity, Double.NEGATIVE_INFINITY.meter.acceleration)
                            )
                    )
                    profile.consolidate()
                    return profile
                }
                maxAcc = abs(goalVel.pow(2) - startState.vel.value.pow(2)) / (2 * deltaPos.meter)
            } else {
                val stoppingTime = abs(startState.vel.value / constraints.maxAccel.value)
                profile.appendControl(-constraints.maxAccel, stoppingTime.second)
                profile.appendProfile(generateFlippedProfile(constraints, profile.endState, endState))
                profile.consolidate()
                return profile
            }
        }
        goalVel = min(goalVel, maxAbsVelAtGoal)

        val vMax = min(constraints.maxVel.value,
                sqrt((startState.vel.value.pow(2) + goalVel.pow(2)) / 2.0 + deltaPos.meter * maxAcc))

        if(vMax > startState.vel.value) {
            val accelTime = (vMax - startState.vel.value) / maxAcc
            profile.appendControl(maxAcc.meter.acceleration, accelTime.second)
            startState = profile.endState
        }

        val distDecel = max(0.0,
                (startState.vel.value.pow(2) - goalVel.pow(2)) / (2.0 * constraints.maxAccel.value))
        val distCruise = max(0.0, endState.pos.meter - startState.pos.meter - distDecel)

        if(distCruise > 0.0) {
            val cruiseTime = distCruise / startState.vel.value
            profile.appendControl(0.meter.acceleration, cruiseTime.second)
            startState = profile.endState
        }

        if(distDecel > 0.0) {
            val decelTime = (startState.vel.value - goalVel) / maxAcc
            profile.appendControl(-maxAcc.meter.acceleration, decelTime.second)
        }

        profile.consolidate()
        return profile
    }
}