package frc.team4069.saturn.lib.motor.ctre

import com.ctre.phoenix.motorcontrol.IMotorController
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.nativeunits.*
import frc.team4069.saturn.lib.motor.AbstractSaturnEncoder
import kotlin.properties.Delegates

class SaturnCTREEncoder<T: SIUnit<T>>(
        val motorController: IMotorController,
        val pidIdx: Int = 0,
        model: NativeUnitModel<T>
) : AbstractSaturnEncoder<T>(model) {
    override val rawVelocity: NativeUnitVelocity
        get() = motorController.getSelectedSensorVelocity(pidIdx).STUPer100ms

    override val rawPosition: NativeUnit
        get() = motorController.getSelectedSensorPosition(pidIdx).STU

    var encoderPhase by Delegates.observable(false) { _, _, newValue ->
        motorController.setSensorPhase(newValue)
    }

    override fun resetPosition(newPosition: Double) {
        motorController.setSelectedSensorPosition(newPosition.toInt(), pidIdx, 0)
    }
}