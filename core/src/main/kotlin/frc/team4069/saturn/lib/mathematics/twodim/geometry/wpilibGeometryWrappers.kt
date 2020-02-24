@file:Suppress("FunctionName")

package frc.team4069.saturn.lib.mathematics.twodim.geometry

import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Translation2d
import frc.team4069.saturn.lib.mathematics.units.*

fun Translation2d(x: SIUnit<Meter> = 0.meter, y: SIUnit<Meter> = 0.meter)
    = edu.wpi.first.wpilibj.geometry.Translation2d(x.value, y.value)

val Translation2d.xU get() = this.x.meter
val Translation2d.yU get() = this.y.meter

fun Rotation2d(theta: SIUnit<Unitless>)
    = Rotation2d(theta.value)

fun Pose2d(x: SIUnit<Meter> = 0.meter, y: SIUnit<Meter> = 0.meter, theta: SIUnit<Unitless> = 0.radian)
    = edu.wpi.first.wpilibj.geometry.Pose2d(x.value, y.value, Rotation2d(theta))

fun Twist2d(dx: SIUnit<Meter>, dy: SIUnit<Meter>, dtheta: SIUnit<Unitless>)
    = edu.wpi.first.wpilibj.geometry.Twist2d(dx.value, dy.value, dtheta.value)

fun Transform2d(x: SIUnit<Meter>, y: SIUnit<Meter>, theta: SIUnit<Unitless> = 0.radian)
    = edu.wpi.first.wpilibj.geometry.Transform2d(Translation2d(x, y), Rotation2d(theta))
