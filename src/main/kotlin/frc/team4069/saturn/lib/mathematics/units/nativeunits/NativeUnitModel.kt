package frc.team4069.saturn.lib.mathematics.units.nativeunits

/* ktlint-disable no-wildcard-imports */
import frc.team4069.saturn.lib.mathematics.TAU
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity

class NativeUnitLengthModel(
        sensorUnitsPerRotation: NativeUnit = kDefaultSensorUnitsPerRotation,
        val wheelRadius: Length = 3.inch
) : NativeUnitModel<Length>(sensorUnitsPerRotation, 0.inch) {
    override fun fromNativeUnitPosition(value: NativeUnit): Length =
            wheelRadius * ((value / sensorUnitsPerRotation) * TAU)

    override fun toNativeUnitPosition(value: Length): NativeUnit =
            sensorUnitsPerRotation * (value / (wheelRadius * TAU))
}

class NativeUnitRotationModel(
        sensorUnitsPerRotation: NativeUnit = kDefaultSensorUnitsPerRotation
) : NativeUnitModel<Rotation2d>(sensorUnitsPerRotation, 0.degree) {
    override fun fromNativeUnitPosition(value: NativeUnit) =
            Rotation2d.kRotation * (value / sensorUnitsPerRotation)

    override fun toNativeUnitPosition(value: Rotation2d) =
            sensorUnitsPerRotation * (value / Rotation2d.kRotation)
}

class NativeUnitSensorModel(sensorUnitsPerRotation: NativeUnit = kDefaultSensorUnitsPerRotation) : NativeUnitModel<NativeUnit>(sensorUnitsPerRotation, 0.STU) {
    override fun fromNativeUnitPosition(value: NativeUnit): NativeUnit {
        return value
    }

    override fun toNativeUnitPosition(value: NativeUnit): NativeUnit {
        return value
    }
}

abstract class NativeUnitModel<T : SIUnit<T>>(
        val sensorUnitsPerRotation: NativeUnit,
        val zero: T
) {
    abstract fun fromNativeUnitPosition(value: NativeUnit): T
    abstract fun toNativeUnitPosition(value: T): NativeUnit

    fun fromNativeUnitVelocity(value: NativeUnitVelocity): Velocity<T> {
        return fromNativeUnitPosition(value.type.createNew(value.value)).velocity
    }

    fun toNativeUnitVelocity(value: Velocity<T>): NativeUnitVelocity {
        return toNativeUnitPosition(value.type.createNew(value.value)).velocity
    }

    fun fromNativeUnitAcceleration(value: NativeUnitAcceleration): Acceleration<T> {
        return fromNativeUnitPosition(value.type.createNew(value.value)).acceleration
    }

    fun toNativeUnitAcceleration(value: Acceleration<T>): NativeUnitAcceleration {
        return toNativeUnitPosition(value.type.createNew(value.value)).acceleration
    }

    companion object {
        val kDefaultSensorUnitsPerRotation = 4096.STU
    }
}