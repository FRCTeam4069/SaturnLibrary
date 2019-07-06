package frc.team4069.saturn.lib.mathematics.units.nativeunits

/* ktlint-disable no-wildcard-imports */
import frc.team4069.saturn.lib.mathematics.TAU
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.inch
import frc.team4069.saturn.lib.mathematics.units.derived.AccelerationT
import frc.team4069.saturn.lib.mathematics.units.derived.VelocityT

class NativeUnitLengthModel(
        sensorUnitsPerRotation: SIUnit<NativeUnit> = kDefaultSensorUnitsPerRotation,
        val wheelRadius: Length = 3.inch
) : NativeUnitModel<Meter>(sensorUnitsPerRotation, 0.inch) {
    override fun fromNativeUnitPosition(value: SIUnit<NativeUnit>): Length =
            wheelRadius * ((value / sensorUnitsPerRotation) * TAU)

    override fun toNativeUnitPosition(value: Length): SIUnit<NativeUnit> =
            sensorUnitsPerRotation * (value / (wheelRadius * TAU))
}

class NativeUnitRotationModel(
        sensorUnitsPerRotation: SIUnit<NativeUnit> = kDefaultSensorUnitsPerRotation
) : NativeUnitModel<Radian>(sensorUnitsPerRotation, 0.degree) {
    override fun fromNativeUnitPosition(value: SIUnit<NativeUnit>) =
            360.degree * (value / sensorUnitsPerRotation)

    override fun toNativeUnitPosition(value: SIUnit<Radian>) =
            sensorUnitsPerRotation * (value / 360.degree)
}

class NativeUnitSensorModel(sensorUnitsPerRotation: SIUnit<NativeUnit> = kDefaultSensorUnitsPerRotation) : NativeUnitModel<NativeUnit>(sensorUnitsPerRotation, 0.STU) {
    override fun fromNativeUnitPosition(value: SIUnit<NativeUnit>): SIUnit<NativeUnit> {
        return value
    }

    override fun toNativeUnitPosition(value: SIUnit<NativeUnit>): SIUnit<NativeUnit> {
        return value
    }
}

abstract class NativeUnitModel<T: Key>(
        val sensorUnitsPerRotation: SIUnit<NativeUnit>,
        val zero: SIUnit<T>
) {
    abstract fun fromNativeUnitPosition(value: SIUnit<NativeUnit>): SIUnit<T>
    abstract fun toNativeUnitPosition(value: SIUnit<T>): SIUnit<NativeUnit>

    fun fromNativeUnitVelocity(value: NativeUnitVelocity): SIUnit<VelocityT<T>> {
        return fromNativeUnitPosition(SIUnit(value.value)).velocity
    }

    fun toNativeUnitVelocity(value: SIUnit<VelocityT<T>>): NativeUnitVelocity {
        return toNativeUnitPosition(SIUnit(value.value)).velocity
    }

    fun fromNativeUnitAcceleration(value: NativeUnitAcceleration): SIUnit<AccelerationT<T>> {
        return fromNativeUnitPosition(SIUnit(value.value)).acceleration
    }

    fun toNativeUnitAcceleration(value: SIUnit<AccelerationT<T>>): NativeUnitAcceleration {
        return toNativeUnitPosition(SIUnit(value.value)).acceleration
    }

    companion object {
        val kDefaultSensorUnitsPerRotation = 4096.STU
    }
}