package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.units.conversions.AngularAccelerationT
import frc.team4069.saturn.lib.mathematics.units.conversions.AngularVelocityT
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearAccelerationT
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocityT
import frc.team4069.saturn.lib.mathematics.units.derived.CurvatureT


typealias Frequency = SIUnit<Inverse<Second>>
typealias Length = SIUnit<Meter>
typealias Time = SIUnit<Second>
typealias LinearVelocity = SIUnit<LinearVelocityT>
typealias LinearAcceleration = SIUnit<LinearAccelerationT>
typealias AngularVelocity = SIUnit<AngularVelocityT>
typealias AngularAcceleration = SIUnit<AngularAccelerationT>
typealias Curvature = SIUnit<CurvatureT>
