package frc.team4069.saturn.lib.mathematics.twodim.geometry

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Twist2d
import frc.team4069.saturn.lib.mathematics.units.hypot
import frc.team4069.saturn.lib.mathematics.units.radian
import frc.team4069.saturn.lib.types.VaryInterpolatable
import kotlin.math.hypot

inline class InterpolatablePose2d(val pose: Pose2d) : VaryInterpolatable<InterpolatablePose2d> {
    override fun distance(other: InterpolatablePose2d): Double {
        val twist = Pose2d().log(other.pose.relativeTo(pose))

        return hypot(twist.dx, twist.dy)
    }

    override fun interpolate(endValue: InterpolatablePose2d, t: Double): InterpolatablePose2d {
        if(t <= 0.0) {
            return InterpolatablePose2d(Pose2d(pose.translation, pose.rotation))
        } else if(t >= 1.0) {
            return InterpolatablePose2d(Pose2d(endValue.pose.translation, endValue.pose.rotation))
        }

        val twist = Pose2d().log(endValue.pose.relativeTo(pose))
        return InterpolatablePose2d(pose.exp(twist * t))
    }

    operator fun Twist2d.times(scale: Double) = Twist2d(dx * scale, dy * scale, dtheta * scale)

    operator fun Pose2d.unaryMinus(): Pose2d {
        val invertedRotation = -this.rotation
        return Pose2d((-translation).rotateBy(invertedRotation), invertedRotation)
    }
}