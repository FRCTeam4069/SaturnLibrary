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

package frc.team4069.saturn.lib.subsystem

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.controller.PIDController
import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds
import edu.wpi.first.wpilibj.trajectory.Trajectory
import frc.team4069.saturn.lib.commands.SaturnCommand
import frc.team4069.saturn.lib.debug.LiveDashboard
import frc.team4069.saturn.lib.mathematics.twodim.geometry.xU
import frc.team4069.saturn.lib.mathematics.twodim.geometry.yU
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.conversions.feet
import frc.team4069.saturn.lib.util.DeltaTime

class TrajectoryTrackerCommand(val driveSubsystem: TankDriveSubsystem,
                               b: Double,
                               zeta: Double,
                               val trajectory: Trajectory,
                               val leftPid: PIDController,
                               val rightPid: PIDController,
                               val feedforward: SimpleMotorFeedforward,
                               val resetPose: Boolean = false,
                               val velocityConsumer: ((SIUnit<LinearVelocity>, SIUnit<LinearVelocity>) -> Unit)? = null) : SaturnCommand(driveSubsystem) {
    private val delta = DeltaTime()
    private lateinit var prevSpeeds: DifferentialDriveWheelSpeeds
    private val timer = Timer()

    private val tracker = RamseteController(b, zeta)

    override fun initialize() {
        LiveDashboard.isFollowingPath = true

        delta.reset()
        timer.reset()
        timer.start()

        leftPid.reset()
        rightPid.reset()

        val firstState = trajectory.sample(0.0)
        prevSpeeds = driveSubsystem.kinematics.toWheelSpeeds(ChassisSpeeds(
                firstState.velocityMetersPerSecond,
                0.0,
                firstState.curvatureRadPerMeter * firstState.velocityMetersPerSecond
        ))

        if(resetPose) {
            driveSubsystem.robotPosition = firstState.poseMeters
        }
    }

    override fun execute() {
        val now = timer.get()
        val dt = delta.updateTime(now.second)

        val refState = trajectory.sample(now)
        val targetWheelSpeeds = driveSubsystem.kinematics.toWheelSpeeds(tracker.calculate(driveSubsystem.robotPosition,
                refState))

        val referencePose = refState.poseMeters
        LiveDashboard.pathX = referencePose.translation.xU.feet
        LiveDashboard.pathY = referencePose.translation.yU.feet
        LiveDashboard.pathHeading = referencePose.rotation.radians

        val leftAccel = (targetWheelSpeeds.leftMetersPerSecond - prevSpeeds.leftMetersPerSecond) / dt.value
        val rightAccel = (targetWheelSpeeds.rightMetersPerSecond - prevSpeeds.rightMetersPerSecond) / dt.value

        val leftOutput = feedforward.calculate(targetWheelSpeeds.leftMetersPerSecond, leftAccel) + leftPid.calculate(driveSubsystem.leftVelocity().value)
        val rightOutput = feedforward.calculate(targetWheelSpeeds.rightMetersPerSecond, rightAccel) + rightPid.calculate(driveSubsystem.rightVelocity().value)
        driveSubsystem.setVoltages(leftOutput.volt, rightOutput.volt)
        velocityConsumer?.invoke(targetWheelSpeeds.leftMetersPerSecond.meter.velocity, targetWheelSpeeds.rightMetersPerSecond.meter.velocity)
    }

    override fun end(interrupted: Boolean) {
        driveSubsystem.setNeutral()
        LiveDashboard.isFollowingPath = false
    }

    override fun isFinished(): Boolean {
        return timer.hasPeriodPassed(trajectory.totalTimeSeconds)
    }
}