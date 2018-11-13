package frc.team4069.visualizer

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.DefaultTrajectoryGenerator
import frc.team4069.saturn.lib.mathematics.units.degree
import frc.team4069.saturn.lib.mathematics.units.derivedunits.acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.feet
import frc.team4069.saturn.lib.mathematics.units.millisecond
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import java.awt.Color
import java.awt.Font

fun main(args: Array<String>) {

    val trajectory = DefaultTrajectoryGenerator.generateTrajectory(
        listOf(
            Pose2d(0.feet, 13.feet),
            Pose2d(5.feet, 10.feet, (-90).degree),
            Pose2d(8.feet, 6.feet),
            Pose2d(12.feet, 6.feet)
        ),
        reversed = false,
        constraints = listOf(),
        startVelocity = 0.feet.velocity,
        endVelocity = 0.feet.velocity,
        maxVelocity = 8.feet.velocity,
        maxAcceleration = 5.feet.acceleration
    )

    val xs = mutableListOf<Double>()
    val ys = mutableListOf<Double>()

    val iter = trajectory.iterator()

    while(!iter.isDone) {
        val point = iter.advance(20.millisecond)
        xs.add(point.state.state.pose.translation.x.feet)
        ys.add(point.state.state.pose.translation.y.feet)
    }


//    plot(xs.toDoubleArray(), ys.toDoubleArray(), color = "b", lineLabel = "Spline")

    val chart = XYChartBuilder()
        .width(1800)
        .height(1520)
        .title("Trajectory visualization")
        .xAxisTitle("X")
        .yAxisTitle("Y")
        .build().apply {
            styler.markerSize = 4
            styler.seriesColors = arrayOf(Color.ORANGE, Color(151, 60, 67))

            styler.chartTitleFont = Font("Kanit", 1, 40)
            styler.chartTitlePadding = 15

            styler.xAxisMin = 1.0
            styler.xAxisMax = 26.0
            styler.yAxisMin = 1.0
            styler.yAxisMax = 26.0

            styler.chartFontColor = Color.WHITE
            styler.axisTickLabelsColor = Color.WHITE

            styler.legendBackgroundColor = Color.GRAY

            styler.isPlotGridLinesVisible = true
            styler.isLegendVisible = true

            styler.plotGridLinesColor = Color.GRAY
            styler.chartBackgroundColor = Color.DARK_GRAY
            styler.plotBackgroundColor = Color.DARK_GRAY
        }

    chart.addSeries("Trajectory", xs, ys)
    SwingWrapper(chart).displayChart()

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
