package frc.team4069.visualizer

import frc.team4069.saturn.lib.math.Pose2d
import frc.team4069.saturn.lib.math.RamsyeetPathFollower
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import koma.cos
import koma.figure
import koma.plot
import koma.sin
import java.io.File

fun main(args: Array<String>) {
    val root = File(".")

    val waypoints = arrayOf(
            Waypoint(11.0, 6.0, 0.0),
            Waypoint(5.0, 6.0, 0.0)
    )

    val path = Pathfinder.generate(waypoints, Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 0.02, baseVelocity.fps, 3.0, 60.0))
    println(path)

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
