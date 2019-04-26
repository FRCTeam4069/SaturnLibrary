package frc.team4069.saturn.lib.motor

import com.revrobotics.CANSparkMax
import com.revrobotics.ControlType
import edu.wpi.first.wpilibj.RobotController
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.volt
import frc.team4069.saturn.lib.mathematics.units.minute
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STU
import frc.team4069.saturn.lib.mathematics.units.nativeunits.fromModel
import frc.team4069.saturn.lib.mathematics.units.nativeunits.toModel

class SaturnMAX<T : SIUnit<T>>(id: Int, motorType: MotorType = MotorType.kBrushless, override val model: NativeUnitModel<T>) : CANSparkMax(id, motorType), SaturnMotor<T> {
    private val pid = pidController
    private val _encoder = encoder // Name shadowing ree

    var kF: Double
        get() = pid.ff
        set(value) {
            pid.ff = value
        }

    /**
     * Whether closed loop should be smart or normal
     */
    var useSmartClosedLoop = true

    override val sensorPosition get() = model.toModel(_encoder.position.STU)
    override val sensorVelocity get() = _encoder.velocity.STU.velocity.toModel(model)
    override val motorOutputVoltage get() = (this.appliedOutput * RobotController.getBatteryVoltage()).volt

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

    override fun setPosition(position: T) = if (useSmartClosedLoop) {
        set(ControlType.kSmartMotion, position)
    } else {
        set(ControlType.kPosition, position)
    }

    override fun setPercentOutput(duty: Double) {
        set(duty)
    }

    override fun setClosedLoopVelocity(velocity: Velocity<T>, arbitraryFeedForward: Double) {
        val vbat = RobotController.getBatteryVoltage()

        //TODO: Maybe use smart velocity?
        pid.setReference(velocity.fromModel(model).value, ControlType.kVelocity, 0, arbitraryFeedForward * vbat)
    }

    fun set(type: ControlType, output: T) {
        pid.setReference(model.fromModel(output).value, type)
    }
}
