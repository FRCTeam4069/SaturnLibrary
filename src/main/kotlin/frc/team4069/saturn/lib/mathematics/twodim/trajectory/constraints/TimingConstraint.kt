/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

package frc.team4069.saturn.lib.mathematics.twodim.trajectory.constraints

interface TimingConstraint<S> {
    fun getMaxVelocity(state: S): Double

    fun getMinMaxAcceleration(state: S, velocity: Double): MinMaxAcceleration

    class MinMaxAcceleration(
        val minAcceleration: Double,
        val maxAcceleration: Double
    ) {
        constructor() : this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)

        val valid = minAcceleration <= maxAcceleration

        companion object {
            val kNoLimits = MinMaxAcceleration()
        }
    }
}