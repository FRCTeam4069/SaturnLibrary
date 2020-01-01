/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package frc.team4069.saturn.lib.motor.rev

import com.revrobotics.*
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.derived.Acceleration
import frc.team4069.saturn.lib.mathematics.units.derived.Velocity
import frc.team4069.saturn.lib.mathematics.units.derived.Volt
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import frc.team4069.saturn.lib.motor.AbstractSaturnMotor
import frc.team4069.saturn.lib.motor.SaturnMotor
import kotlin.properties.Delegates

/**
 * A wrapper over the Spark MAX motor controller.
 *
 * The specified [encoderConfig] in the constructor will determine how [encoder] is created.
 */
class SaturnMAX<T : Key>(
        val canSparkMax: CANSparkMax,
        val model: NativeUnitModel<T>,
        encoderConfig: SaturnMAXEncoderConfig = SaturnMAXEncoderConfig.HallEffectEncoder
) : AbstractSaturnMotor<T>() {
    constructor(id: Int, motorType: CANSparkMaxLowLevel.MotorType, model: NativeUnitModel<T>) : this(CANSparkMax(id, motorType), model)

    val controller: CANPIDController = canSparkMax.pidController

    /**
     * The encoder attached to the motor controller.
     *
     * If the configuration given in the creation of this instance was [SaturnMAXEncoderConfig.HallEffectEncoder], the NEO internal sensor is used.
     * If it was [SaturnMAXEncoderConfig.AlternateEncoder], the encoder connected to the data port will be used, and configured with the specified cpr.
     */
    override val encoder = when (encoderConfig) {
        is SaturnMAXEncoderConfig.HallEffectEncoder -> SaturnMAXEncoder(canSparkMax.encoder, model)
        is SaturnMAXEncoderConfig.AlternateEncoder -> SaturnMAXEncoder(canSparkMax.getAlternateEncoder(AlternateEncoderType.kQuadrature, encoderConfig.cpr), model)
    }

    override val voltageOutput: SIUnit<Volt>
        get() = canSparkMax.appliedOutput * canSparkMax.busVoltage.volt

    override val drawnCurrent: SIUnit<Ampere>
        get() = canSparkMax.outputCurrent.amp

    override var outputInverted: Boolean by Delegates.observable(false) { _, _, newValue ->
        canSparkMax.inverted = newValue
    }

    override var brakeMode: Boolean by Delegates.observable(false) { _, _, newValue ->
        canSparkMax.idleMode = if (newValue) CANSparkMax.IdleMode.kBrake else CANSparkMax.IdleMode.kCoast
    }

    override var voltageCompSaturation: SIUnit<Volt> by Delegates.observable(12.volt) { _, _, newValue ->
        canSparkMax.enableVoltageCompensation(newValue.value)
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

    override fun setVoltage(voltage: SIUnit<Volt>, arbitraryFeedForward: SIUnit<Volt>) {
        controller.setReference(voltage.value, ControlType.kVoltage, 0, arbitraryFeedForward.value)
    }

    override fun setDutyCycle(dutyCycle: Double, arbitraryFeedForward: SIUnit<Volt>) {
        controller.setReference(dutyCycle, ControlType.kDutyCycle, 0, arbitraryFeedForward.value)
    }

    override fun setVelocity(velocity: SIUnit<Velocity<T>>, arbitraryFeedForward: SIUnit<Volt>) {
        controller.setReference(
                model.toNativeUnitVelocity(velocity).value * 60,
                ControlType.kVelocity, 0, arbitraryFeedForward.value
        )
    }

    override fun setPosition(position: SIUnit<T>, arbitraryFeedForward: SIUnit<Volt>) {
        controller.setReference(
                model.toNativeUnitPosition(position).value,
                if (useMotionProfileForPosition) ControlType.kSmartMotion else ControlType.kPosition,
                0, arbitraryFeedForward.value
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
