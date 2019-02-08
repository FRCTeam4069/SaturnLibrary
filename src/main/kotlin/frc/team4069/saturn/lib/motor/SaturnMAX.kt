package frc.team4069.saturn.lib.motor

import com.revrobotics.CANSparkMax
import com.revrobotics.ControlType
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity
import frc.team4069.saturn.lib.mathematics.units.minute
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STU
import frc.team4069.saturn.lib.mathematics.units.nativeunits.toModel

class SaturnMAX<T: SIUnit<T>>(id: Int, motorType: MotorType = MotorType.kBrushless, val model: NativeUnitModel<T>) : CANSparkMax(id, motorType) {
    private val pid = pidController
    private val _encoder = encoder // Name shadowing ree

    var kF: Double
        get() = pid.ff
        set(value) {
            pid.ff = value
        }

    var kP: Double
        get() = pid.p
        set(value) {
            pid.p = value
        }

    var kI: Double
        get() = pid.i
        set(value) {
            pid.i = value
        }

    var kD: Double
        get() = pid.d
        set(value) {
            pid.d = value
        }

    var outputRange: Pair<Double, Double>
        get() = pid.outputMin to pid.outputMax
        set(value) {
            pid.setOutputRange(value.first, value.second)
        }

    val position: T
        get() = -_encoder.position.STU.toModel(model) // Bug in MAX firmware causing these values to be incorrect

    val velocity: Velocity<T>
        get() = -(_encoder.velocity.STU / 1.minute).toModel(model) // RPM in FRC :screm:

    fun set(type: ControlType, output: T) {
        pid.setReference(model.fromModel(output).value, type)
    }
}
