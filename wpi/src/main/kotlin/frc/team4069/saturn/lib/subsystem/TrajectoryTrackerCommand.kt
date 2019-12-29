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

import edu.wpi.first.wpilibj.trajectory.Trajectory
import frc.team4069.saturn.lib.commands.SaturnCommand
import frc.team4069.saturn.lib.debug.LiveDashboard
import frc.team4069.saturn.lib.mathematics.twodim.control.TrajectoryTracker
import frc.team4069.saturn.lib.mathematics.twodim.geometry.x
import frc.team4069.saturn.lib.mathematics.twodim.geometry.y
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.Second
import frc.team4069.saturn.lib.mathematics.units.conversions.feet
import frc.team4069.saturn.lib.mathematics.units.milli
import frc.team4069.saturn.lib.mathematics.units.radian
import frc.team4069.saturn.lib.util.Source

class TrajectoryTrackerCommand(val driveSubsystem: TankDriveSubsystem,
                               val trajectory: Source<Trajectory>,
                               val dt: SIUnit<Second> = 20.milli.second) : SaturnCommand(driveSubsystem) {
    private lateinit var tracker: TrajectoryTracker
    private var finished = false

    override fun initialize() {
        tracker = driveSubsystem.trajectoryTracker
        tracker.reset(trajectory())
        LiveDashboard.isFollowingPath = true
    }

    override fun execute() {
        driveSubsystem.setOutput(tracker.nextState(driveSubsystem.robotPosition))

        val referencePose = tracker.referencePoint!!.poseMeters
        LiveDashboard.pathX = referencePose.translation.x().feet
        LiveDashboard.pathY = referencePose.translation.y().feet
        LiveDashboard.pathHeading = referencePose.rotation.radians

        finished = tracker.isFinished
    }

    override fun end(interrupted: Boolean) {
        driveSubsystem.setNeutral()
        LiveDashboard.isFollowingPath = false
    }
}