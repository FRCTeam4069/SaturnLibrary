package frc.team4069.saturn.lib.motor.rev

import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.revrobotics.ControlType
import frc.team4069.saturn.lib.mathematics.units.Key
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.acceleration
import frc.team4069.saturn.lib.mathematics.units.derived.Acceleration
import frc.team4069.saturn.lib.mathematics.units.derived.Velocity
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import frc.team4069.saturn.lib.mathematics.units.velocity
import frc.team4069.saturn.lib.motor.AbstractSaturnMotor
import frc.team4069.saturn.lib.motor.SaturnMotor
import kotlin.properties.Delegates

class SaturnMAX<T : Key>(
        val canSparkMax: CANSparkMax,
        val model: NativeUnitModel<T>
) : AbstractSaturnMotor<T>() {
    constructor(id: Int, motorType: CANSparkMaxLowLevel.MotorType, model: NativeUnitModel<T>) : this(CANSparkMax(id, motorType), model)

    private val controller: CANPIDController = canSparkMax.pidController
    override val encoder = SaturnMAXEncoder(canSparkMax.encoder, model)

    override val voltageOutput: Double
        get() = canSparkMax.appliedOutput * canSparkMax.busVoltage

    override var outputInverted: Boolean by Delegates.observable(false) { _, _, newValue ->
        canSparkMax.inverted = newValue
    }

    override var brakeMode: Boolean by Delegates.observable(false) { _, _, newValue ->
        canSparkMax.idleMode = if (newValue) CANSparkMax.IdleMode.kBrake else CANSparkMax.IdleMode.kCoast
    }

    override var voltageCompSaturation: Double by Delegates.observable(12.0) { _, _, newValue ->
        canSparkMax.enableVoltageCompensation(newValue)
    }

    override var motionProfileCruiseVelocity: SIUnit<Velocity<T>> by Delegates.observable(model.zero.velocity) { _, _, newValue ->
        controller.setSmartMotionMaxVelocity(model.toNativeUnitVelocity(newValue).value * 60, 0)
    }
    override var motionProfileAcceleration: SIUnit<Acceleration<T>> by Delegates.observable(model.zero.acceleration) { _, _, newValue ->
        controller.setSmartMotionMaxAccel(model.toNativeUnitAcceleration(newValue).value * 60.0, 0)
    }

    init {
        canSparkMax.enableVoltageCompensation(12.0)
    }

    override fun setVoltage(voltage: Double, arbitraryFeedForward: Double) {
        controller.setReference(voltage, ControlType.kVoltage, 0, arbitraryFeedForward)
    }

    override fun setDutyCycle(dutyCycle: Double, arbitraryFeedForward: Double) {
        controller.setReference(dutyCycle, ControlType.kDutyCycle, 0, arbitraryFeedForward)
    }

    override fun setVelocity(velocity: SIUnit<Velocity<T>>, arbitraryFeedForward: Double) {
        controller.setReference(
                model.toNativeUnitVelocity(velocity).value * 60,
                ControlType.kVelocity, 0, arbitraryFeedForward
        )
    }

    override fun setPosition(position: SIUnit<T>, arbitraryFeedForward: Double) {
        controller.setReference(
                model.toNativeUnitPosition(position).value,
                if (useMotionProfileForPosition) ControlType.kSmartMotion else ControlType.kPosition,
                0, arbitraryFeedForward
        )
    }

    override fun setNeutral() = setDutyCycle(0.0)

    override fun follow(motor: SaturnMotor<*>): Boolean =
            if (motor is SaturnMAX<*>) {
                canSparkMax.follow(motor.canSparkMax)
                true
            } else {
                super.follow(motor)
            }
}
