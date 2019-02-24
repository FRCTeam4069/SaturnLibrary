package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.epsilonEquals
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.meter
import frc.team4069.saturn.lib.mathematics.units.second
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

data class MotionState(val t: Time, val pos: Length, val vel: LinearVelocity, val acc: LinearAcceleration) {
    fun extrapolate(t: Time): MotionState {
        return extrapolate(t, acc)
    }

    fun extrapolate(t: Time, acc: LinearAcceleration): MotionState {
        val dt = (t - this.t).second
        val velDt = vel.value * dt // Integrated velocity
        val accDt = acc.value * dt // Integrated
        val accDt2 = 0.5 * acc.value * dt.pow(2) // Double integrated acceleration
        return MotionState(t, (pos.meter + velDt + accDt2).meter, (vel.value + accDt).meter.velocity, acc)
    }

    fun nextTimeAtPos(pos: Length): Time {
        if (this.pos.meter epsilonEquals pos.meter) {
            return t
        }

        val vel = vel.value
        val acc = acc.value
        val t = t.second

        if (acc epsilonEquals 0.0) {
            val ds = (pos - this.pos).meter
            if (!(vel epsilonEquals 0.0) && sign(ds) == sign(vel)) {
                return (ds / vel + t).second
            }
            return Double.NaN.second
        }

        val disc = vel.pow(2) - 2.0 * acc * (this.pos.meter - pos.meter)
        if (disc < 0.0) {
            return Double.NaN.second
        }

        val sqrtDisc = sqrt(disc)
        val maxDt = (-vel + sqrtDisc) / acc
        val minDt = (-vel - sqrtDisc) / acc
        if (minDt >= 0.0 && (maxDt < 0.0 || minDt < maxDt)) {
            return (t + minDt).second
        }
        if(maxDt >= 0.0) {
            return (t + maxDt).second
        }

        return Double.NaN.second
    }

    fun coincident(other: MotionState): Boolean {
        return t.second epsilonEquals other.t.second && pos.meter epsilonEquals other.pos.meter
                && vel.value epsilonEquals other.vel.value
    }

    val flipped = MotionState(t, -pos, -vel, -acc)

    companion object {
        val kInvalidState = MotionState(Double.NaN.second, Double.NaN.meter,
                Double.NaN.meter.velocity, Double.NaN.meter.acceleration)
    }
}