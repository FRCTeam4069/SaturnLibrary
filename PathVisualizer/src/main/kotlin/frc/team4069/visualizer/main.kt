package frc.team4069.visualizer

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.DefaultTrajectoryGenerator
import frc.team4069.saturn.lib.mathematics.units.degree
import frc.team4069.saturn.lib.mathematics.units.feet

fun main(args: Array<String>) {

    val trajectory = DefaultTrajectoryGenerator.generateTrajectory(
        listOf(
        Pose2d(0.feet, 13.feet),
        Pose2d(5.feet, 10.feet, (-90).degree),
        Pose2d(8.feet, 6.feet, (-90).degree),
        Pose2d(12.feet, 6.feet))
    )

//    val xs = path.segments.map(Trajectory.Segment::x)
//    val ys = path.segments.map(Trajectory.Segment::y)
//
//    val ramseteXs = mutableListOf<Double>()
//    val ramseteYs = mutableListOf<Double>()
//
//    val follower = RamsyeetPathFollower(path, 0.8, 0.75)
//
//    val first = path[0]
//    val pose = Pose2d(first.x, first.y, 0.0)
//
//    val dt = 0.02
//    var i = 0
//
//    while (!follower.isFinished) {
//        ramseteXs += pose.x
//        ramseteYs += pose.y
//
//        val twist = follower.update(pose)
//
//        pose.theta += twist.w * dt
//        pose.x += twist.v * dt * cos(pose.theta)
//        pose.y += twist.v * dt * sin(pose.theta)
//
//        i++
//    }
//
//    figure(1)
//    plot(xs.toDoubleArray(), ys.toDoubleArray(), color = "b", lineLabel = "Pathfinder Path")
//    plot(ramseteXs.toDoubleArray(), ramseteYs.toDoubleArray(), color = "o", lineLabel = "Robot Position")
}
