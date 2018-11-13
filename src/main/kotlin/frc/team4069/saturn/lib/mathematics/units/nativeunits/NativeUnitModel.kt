package frc.team4069.saturn.lib.mathematics.units.nativeunits

/* ktlint-disable no-wildcard-imports */
import frc.team4069.saturn.lib.mathematics.units.*

class NativeUnitLengthModel(
    sensorUnitsPerRotation: NativeUnit = kDefaultSensorUnitsPerRotation,
    val wheelRadius: Length = 3.inch
) : NativeUnitModel<Length>(sensorUnitsPerRotation, 0.inch) {
    override fun toModel(value: NativeUnit): Length =
            wheelRadius * ((value / sensorUnitsPerRotation) * (2.0 * Math.PI))

    override fun fromModel(value: Length): NativeUnit =
            sensorUnitsPerRotation * (value / (wheelRadius * (2.0 * Math.PI)))
}

class NativeUnitRotationModel(
    sensorUnitsPerRotation: NativeUnit = kDefaultSensorUnitsPerRotation
) : NativeUnitModel<Rotation2d>(sensorUnitsPerRotation, 0.degree) {
    override fun toModel(value: NativeUnit) =
            Rotation2d.kRotation * (value / sensorUnitsPerRotation)

    override fun fromModel(value: Rotation2d) =
            sensorUnitsPerRotation * (value / Rotation2d.kRotation)
}

abstract class NativeUnitModel<T : SIUnit<T>>(
    val sensorUnitsPerRotation: NativeUnit,
    val zero: T
) {
    abstract fun toModel(value: NativeUnit): T
    abstract fun fromModel(value: T): NativeUnit

    companion object {
        val kDefaultSensorUnitsPerRotation = 1440.STU
    }
}