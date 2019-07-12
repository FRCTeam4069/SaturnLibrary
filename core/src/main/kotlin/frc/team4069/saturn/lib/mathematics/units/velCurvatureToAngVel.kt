package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.units.conversions.AngularVelocity
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.derived.Curvature

operator fun SIUnit<LinearVelocity>.times(curvature: SIUnit<Curvature>) = SIUnit<AngularVelocity>(value * curvature.value)
