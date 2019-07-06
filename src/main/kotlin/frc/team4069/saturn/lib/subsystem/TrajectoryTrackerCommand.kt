package frc.team4069.saturn.lib.subsystem

import frc.team4069.saturn.lib.commands.SaturnCommand
import frc.team4069.saturn.lib.debug.LiveDashboard
import frc.team4069.saturn.lib.mathematics.twodim.control.TrajectoryTracker
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedTrajectory
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.millisecond
import frc.team4069.saturn.lib.util.Source

class TrajectoryTrackerCommand(val driveSubsystem: TankDriveSubsystem,
                               val trajectory: Source<TimedTrajectory<Pose2dWithCurvature>>,
                               val dt: Time = 20.millisecond) : SaturnCommand(driveSubsystem) {
    private lateinit var tracker: TrajectoryTracker
    private var finished = false

    override fun initialize() {
        tracker = driveSubsystem.trajectoryTracker
        tracker.reset(trajectory())
        LiveDashboard.isFollowingPath = true
    }

    override fun execute() {
        driveSubsystem.setOutput(tracker.update(driveSubsystem.robotPosition))

        val referencePose = tracker.referencePoint!!.state.state.pose
        LiveDashboard.pathX = referencePose.translation.x.feet
        LiveDashboard.pathY = referencePose.translation.y.feet
        LiveDashboard.pathHeading = referencePose.rotation.radian

        finished = tracker.isFinished
    }

    override fun end(interrupted: Boolean) {
        driveSubsystem.setNeutral()
        LiveDashboard.isFollowingPath = false
    }
}