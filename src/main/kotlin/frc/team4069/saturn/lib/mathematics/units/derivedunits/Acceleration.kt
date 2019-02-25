package frc.team4069.saturn.lib.mathematics.units.derivedunits

import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.Rotation2d
import frc.team4069.saturn.lib.mathematics.units.SIValue
import frc.team4069.saturn.lib.mathematics.units.Time

val <T : SIValue<T>> T.acceleration: Acceleration<T> get() = Acceleration(value, this)
val Length.acceleration: LinearAcceleration get() = Acceleration(value, this)
val Rotation2d.acceleration: AngularAcceleration get() = Acceleration(value, this)

typealias LinearAcceleration = Acceleration<Length>
typealias AngularAcceleration = Acceleration<Rotation2d>

class Acceleration<T : SIValue<T>>(
        override val value: Double,
        internal val type: T
) : SIValue<Acceleration<T>> {
    override fun createNew(newValue: Double) = Acceleration(newValue, type)

    operator fun times(other: Time) = Velocity(value * other.value, type)
}