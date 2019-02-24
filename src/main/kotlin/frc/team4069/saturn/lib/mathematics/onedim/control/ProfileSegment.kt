package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.Time
import kotlin.math.sign

data class ProfileSegment(var start: MotionState, var end: MotionState) {
    fun isValid(): Boolean {
        if(!(start.acc epsilonEquals end.acc)) {
            println("Segment acceleration not constant! Start ${start.acc.value} ms^-2. End ${end.acc.value} ms^-2")
            return false
        }
        if(sign(start.vel.value) * sign(end.vel.value) < 0.0 && !(start.vel.value epsilonEquals 0.0)) {
            println("Segment velocity reverses! Start vel ${start.vel.value}. End vel ${end.vel.value}")
            return false
        }

        if(start.extrapolate(end.t) != end) {
            if(start.t == end.t && start.acc.value.isInfinite()) {
                return true
            }
            println("Segment not consistent! Start $start. End $end")
            return false
        }

        return true
    }

    fun containsTime(t: Time): Boolean {
        return t >= start.t && t <= end.t
    }

    fun containsPos(pos: Length): Boolean {
        return pos >= start.pos && pos <= end.pos || pos <= start.pos && pos >= end.pos
    }
}