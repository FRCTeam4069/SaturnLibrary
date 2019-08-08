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

/*
 * Some implementations and algorithms borrowed from:
 * NASA Ames Robotics "The Cheesy Poofs"
 * Team 254
 */

package frc.team4069.saturn.lib.mathematics.twodim.trajectory

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.toRotation2d
import frc.team4069.saturn.lib.mathematics.twodim.polynomials.ParametricQuinticHermiteSpline
import frc.team4069.saturn.lib.mathematics.twodim.polynomials.ParametricSpline
import frc.team4069.saturn.lib.mathematics.twodim.polynomials.ParametricSplineGenerator
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.constraints.TimingConstraint
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.DistanceTrajectory
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.IndexedTrajectory
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedEntry
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedTrajectory
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.conversions.inch
import frc.team4069.saturn.lib.types.VaryInterpolatable
import kotlin.math.absoluteValue
import kotlin.math.pow

val DefaultTrajectoryGenerator = TrajectoryGenerator(
        2.inch,
        0.25.inch,
        Rotation2d.fromDegrees(5.0)
)

class TrajectoryGenerator(
        val kMaxDx: SIUnit<Meter>,
        val kMaxDy: SIUnit<Meter>,
        val kMaxDTheta: Rotation2d
) {

    fun generateTrajectory(
            wayPoints: List<Pose2d>,
            constraints: List<TimingConstraint<Pose2dWithCurvature>>,
            startVelocity: SIUnit<LinearVelocity>,
            endVelocity: SIUnit<LinearVelocity>,
            maxVelocity: SIUnit<LinearVelocity>,
            maxAcceleration: SIUnit<LinearAcceleration>,
            reversed: Boolean
    ): TimedTrajectory<Pose2dWithCurvature> {
        val flippedPosition = Pose2d(rotation = Rotation2d.fromDegrees(180.0))

        // Make theta normal for trajectory generation if path is trajectoryReversed.
        val newWayPoints = wayPoints.asSequence().map { point ->
            if (reversed) point + flippedPosition else point
        }

        var trajectory = trajectoryFromSplineWaypoints(newWayPoints).points

        // After trajectory generation, flip theta back so it's relative to the field.
        // Also fix curvature and its derivative
        if (reversed) {
            trajectory = trajectory.map { state ->
                Pose2dWithCurvature(
                        pose = state.pose + flippedPosition,
                        curvature = -state.curvature
                )
            }
        }

        return timeParameterizeTrajectory(
                DistanceTrajectory(trajectory),
                constraints,
                startVelocity.value,
                endVelocity.value,
                maxVelocity.value,
                maxAcceleration.value.absoluteValue,
                kMaxDx.value,
                reversed
        )
    }

    private fun trajectoryFromSplineWaypoints(
            wayPoints: Sequence<Pose2d>
    ): IndexedTrajectory<Pose2dWithCurvature> {
        val splines = wayPoints.zipWithNext { a, b -> ParametricQuinticHermiteSpline(a, b) }.toMutableList()

        ParametricQuinticHermiteSpline.optimizeSpline(splines)
        return trajectoryFromSplines(splines)
    }

    private fun trajectoryFromSplines(
            splines: List<ParametricSpline>
    ) = IndexedTrajectory(
            ParametricSplineGenerator.parameterizeSplines(
                    splines,
                    kMaxDx,
                    kMaxDy,
                    kMaxDTheta
            )
    )

    private fun <S : VaryInterpolatable<S>> timeParameterizeTrajectory(
            distanceTrajectory: DistanceTrajectory<S>,
            constraints: List<TimingConstraint<S>>,
            startVelocity: Double,
            endVelocity: Double,
            maxVelocity: Double,
            maxAcceleration: Double,
            stepSize: Double,
            reversed: Boolean
    ): TimedTrajectory<S> {

        class ConstrainedState<S : VaryInterpolatable<S>> {
            lateinit var state: S
            var distance: Double = 0.0
            var maxVelocity: Double = 0.0
            var minAcceleration: Double = 0.0
            var maxAcceleration: Double = 0.0

            override fun toString(): String {
                return state.toString() + ", distance: " + distance + ", maxVelocity: " + maxVelocity + ", " +
                        "minAcceleration: " + minAcceleration + ", maxAcceleration: " + maxAcceleration
            }
        }

        fun enforceAccelerationLimits(
                reverse: Boolean,
                constraints: List<TimingConstraint<S>>,
                constraintState: ConstrainedState<S>
        ) {

            for (constraint in constraints) {
                val minMaxAccel = constraint.getMinMaxAcceleration(
                        constraintState.state,
                        (if (reverse) -1.0 else 1.0) * constraintState.maxVelocity
                )
                if (!minMaxAccel.valid) {
                    throw RuntimeException()
                }
                constraintState.minAcceleration = Math.max(
                        constraintState.minAcceleration,
                        if (reverse) -minMaxAccel.maxAcceleration else minMaxAccel.minAcceleration
                )
                constraintState.maxAcceleration = Math.min(
                        constraintState.maxAcceleration,
                        if (reverse) -minMaxAccel.minAcceleration else minMaxAccel.maxAcceleration
                )
            }
        }

        val distanceViewRange =
                distanceTrajectory.firstInterpolant.value..distanceTrajectory.lastInterpolant.value
        val distanceViewSteps =
                Math.ceil((distanceTrajectory.lastInterpolant.value - distanceTrajectory.firstInterpolant.value) / stepSize + 1)
                        .toInt()

        val states = (0 until distanceViewSteps).map { step ->
            distanceTrajectory.sample(
                    (step * stepSize + distanceTrajectory.firstInterpolant.value).coerceIn(
                            distanceViewRange
                    )
            )
                    .state
        }

        val constraintStates = ArrayList<ConstrainedState<S>>(states.size)
        val epsilon = 1E-6

        // Forward pass. We look at pairs of consecutive states, where the start state has already been velocity
        // parameterized (though we may adjust the velocity downwards during the backwards pass). We wish to find an
        // acceleration that is admissible at both the start and end state, as well as an admissible end velocity. If
        // there is no admissible end velocity or acceleration, we set the end velocity to the state's maximum allowed
        // velocity and will repair the acceleration during the backward pass (by slowing down the predecessor).

        var predecessor = ConstrainedState<S>()
        predecessor.state = states[0]
        predecessor.distance = 0.0
        predecessor.maxVelocity = startVelocity
        predecessor.minAcceleration = -maxAcceleration
        predecessor.maxAcceleration = maxAcceleration

        for (i in states.indices) {
            // Add the new state.
            constraintStates.add(ConstrainedState())
            val constraintState = constraintStates[i]
            constraintState.state = states[i]
            val ds = constraintState.state.distance(predecessor.state)
            constraintState.distance = ds + predecessor.distance

            // We may need to iterate to find the maximum end velocity and common acceleration, since acceleration
            // limits may be a function of velocity.
            while (true) {
                // Enforce global max velocity and max reachable velocity by global acceleration limit.
                // vf = sqrt(vi^2 + 2*a*d)
                constraintState.maxVelocity = Math.min(
                        maxVelocity,
                        Math.sqrt(predecessor.maxVelocity.pow(2) + 2.0 * predecessor.maxAcceleration * ds)
                )
                if (constraintState.maxVelocity.isNaN()) {
                    throw RuntimeException()
                }
                // Enforce global max absolute acceleration.
                constraintState.minAcceleration = -maxAcceleration
                constraintState.maxAcceleration = maxAcceleration

                // At this point, the state is full constructed, but no constraints have been applied aside from
                // predecessor
                // state max accel.

                // Enforce all velocity constraints.
                for (constraint in constraints) {
                    constraintState.maxVelocity = Math.min(
                            constraintState.maxVelocity,
                            constraint.getMaxVelocity(constraintState.state)
                    )
                }
                if (constraintState.maxVelocity < 0.0) {
                    // This should never happen if constraints are well-behaved.
                    throw RuntimeException()
                }

                // Now enforce all acceleration constraints.
                enforceAccelerationLimits(reversed, constraints, constraintState)
                if (constraintState.minAcceleration > constraintState.maxAcceleration) {
                    // This should never happen if constraints are well-behaved.
                    throw RuntimeException()
                }

                if (ds < epsilon) {
                    break
                }

                // If the max acceleration for this constraint state is more conservative than what we had applied, we
                // need to reduce the max accel at the predecessor state and try again.
                val actualAcceleration =
                        (constraintState.maxVelocity.pow(2) - predecessor.maxVelocity.pow(2)) / (2.0 * ds)
                if (constraintState.maxAcceleration < actualAcceleration - epsilon) {
                    predecessor.maxAcceleration = constraintState.maxAcceleration
                } else {
                    if (actualAcceleration > predecessor.minAcceleration + epsilon) {
                        predecessor.maxAcceleration = actualAcceleration
                    }
                    // If actual acceleration is less than predecessor min accel, we will repair during the backward
                    // pass.
                    break
                }
                // System.out.println("(intermediate) i: " + i + ", " + constraint_state.toString());
            }
            // System.out.println("i: " + i + ", " + constraint_state.toString());
            predecessor = constraintState
        }

        // Backward pass.
        var successor = ConstrainedState<S>()
        successor.state = states[states.size - 1]
        successor.distance = constraintStates[states.size - 1].distance
        successor.maxVelocity = endVelocity
        successor.minAcceleration = -maxAcceleration
        successor.maxAcceleration = maxAcceleration
        for (i in states.indices.reversed()) {
            val constraintState = constraintStates[i]
            val ds = constraintState.distance - successor.distance // will be negative.

            while (true) {
                // Enforce reverse max reachable velocity limit.
                // vf = sqrt(vi^2 + 2*a*d), where vi = successor.
                val newMaxVelocity = Math.sqrt(successor.maxVelocity.pow(2) + 2.0 * successor.minAcceleration * ds)
                if (newMaxVelocity >= constraintState.maxVelocity) {
                    // No new limits to impose.
                    break
                }
                constraintState.maxVelocity = newMaxVelocity
                if (java.lang.Double.isNaN(constraintState.maxVelocity)) {
                    throw RuntimeException()
                }

                // Now check all acceleration constraints with the lower max velocity.
                enforceAccelerationLimits(reversed, constraints, constraintState)
                if (constraintState.minAcceleration > constraintState.maxAcceleration) {
                    throw RuntimeException()
                }

                if (ds > epsilon) {
                    break
                }
                // If the min acceleration for this constraint state is more conservative than what we have applied, we
                // need to reduce the min accel and try again.
                val actualAcceleration =
                        (constraintState.maxVelocity.pow(2) - successor.maxVelocity.pow(2)) / (2.0 * ds)
                if (constraintState.minAcceleration > actualAcceleration + epsilon) {
                    successor.minAcceleration = constraintState.minAcceleration
                } else {
                    successor.minAcceleration = actualAcceleration
                    break
                }
            }
            successor = constraintState
        }

        // Integrate the constrained states forward in time to obtain the TimedStates.
        val timedStates = ArrayList<TimedEntry<S>>(states.size)
        var t = 0.0
        var s = 0.0
        var v = 0.0
        for (i in states.indices) {
            val constrainedState = constraintStates[i]
            // Advance t.
            val ds = constrainedState.distance - s
            val accel = (constrainedState.maxVelocity.pow(2) - v.pow(2)) / (2.0 * ds)
            var dt = 0.0
            if (i > 0) {
                timedStates[i - 1] = timedStates[i - 1].copy(
                        acceleration = (if (reversed) -accel else accel).meter.acceleration
                )

                dt = when {
                    Math.abs(accel) > epsilon -> (constrainedState.maxVelocity - v) / accel
                    Math.abs(v) > epsilon -> ds / v
                    else -> throw RuntimeException()
                }
            }
            t += dt
            if (t.isNaN() || t.isInfinite()) {
                throw RuntimeException()
            }

            v = constrainedState.maxVelocity
            s = constrainedState.distance
            timedStates.add(
                    TimedEntry(
                            constrainedState.state,
                            t.second,
                            (if (reversed) -v else v).meter.velocity,
                            (if (reversed) -accel else accel).meter.acceleration
                    )
            )
        }
        return TimedTrajectory(timedStates)
    }
}