package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.kEpsilon
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.meter
import frc.team4069.saturn.lib.mathematics.units.second
import kotlin.math.min

class MotionProfile(internal val segments: MutableList<ProfileSegment>) {
    constructor() : this(mutableListOf())

    val startState: MotionState
        get()  {
            if(isEmpty()) {
                return MotionState.kInvalidState
            }

            return segments.first().start
        }

    val startTime: Time
        get() = startState.t

    val startPos: Length
        get() = startState.pos

    val endState: MotionState
        get() {
            if(isEmpty()) {
                return MotionState.kInvalidState
            }
            return segments.last().end
        }

    val endTime: Time
        get() = endState.t

    val endPos: Length
        get() = endState.pos

    fun isValid(): Boolean {
        var prevSegment: ProfileSegment? = null

        for(s in segments) {
            if(!s.isValid()) {
                return false
            }

            if(prevSegment != null && !s.start.coincident(prevSegment.end)) {
                println("Segment not continuous. E: ${prevSegment.end}. S: ${s.start}")
                return false
            }

            prevSegment = s
        }

        return true
    }

    fun isEmpty() = segments.isEmpty()

    fun stateByTime(t: Time): MotionState? {
        if(t < startTime && t + kEpsilon.second >= startTime) {
            return startState
        }
        if(t > endTime && t - kEpsilon.second <= endTime) {
            return endState
        }

        for(s in segments) {
            if(s.containsTime(t)) {
                return s.start.extrapolate(t)
            }
        }

        return null
    }

    fun stateByTimeClamped(t: Time): MotionState {
        if(t < startTime) {
            return startState
        }
        if(t > endTime) {
            return endState
        }

        for(s in segments) {
            if(s.containsTime(t)) {
                return s.start.extrapolate(t)
            }
        }

        return MotionState.kInvalidState
    }

    fun firstStateByPos(pos: Length): MotionState? {
        for(s in segments) {
            if(s.containsPos(pos)) {
                if(s.end.pos epsilonEquals pos) {
                    return s.end
                }
                val t = min(s.start.nextTimeAtPos(pos).second, s.end.t.second)
                if(t.isNaN()) {
                    println("Error! we should reach 'pos' but we don't")
                    return null
                }
                return s.start.extrapolate(t.second)
            }
        }

        return null
    }

    fun trimBeforeTime(t: Time) {
        val it = segments.iterator()
        while(it.hasNext()) {
            val s = it.next()
            if(s.end.t <= t) {
                it.remove()
                continue
            }

            if(s.start.t <= t) {
                s.start = s.start.extrapolate(t)
            }
            break
        }
    }

    fun clear() {
        segments.clear()
    }

    fun reset(initialState: MotionState) {
        clear()
        segments.add(ProfileSegment(initialState, initialState))
    }

    fun consolidate() {
        val it = segments.iterator()
        while(it.hasNext()) {
            val s = it.next()
            if(s.start.coincident(s.end)) {
                it.remove()
            }
        }
    }

    fun appendControl(acc: LinearAcceleration, dt: Time) {
        val lastEndState = segments.last().end
        val newStartState = MotionState(lastEndState.t, lastEndState.pos, lastEndState.vel, acc)
        appendSegment(ProfileSegment(newStartState, newStartState.extrapolate(newStartState.t + dt)))
    }

    fun appendSegment(seg: ProfileSegment) {
        segments.add(seg)
    }

    fun appendProfile(profile: MotionProfile) {
        for(s in profile.segments) {
            appendSegment(s)
        }
    }

    val size: Int get() = segments.size

    val duration: Time
        get() = endTime - startTime

    val length: Length
        get() {
            var len = 0.meter
            for(s in segments) {
                len += (s.end.pos - s.start.pos).absoluteValue
            }

            return len
        }
}
