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

import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.revrobotics.ControlType
import frc.team4069.saturn.lib.mathematics.units.Key
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.acceleration
import frc.team4069.saturn.lib.mathematics.units.derived.Acceleration
import frc.team4069.saturn.lib.mathematics.units.derived.Velocity
import frc.team4069.saturn.lib.mathematics.units.derived.Volt
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

    val controller: CANPIDController = canSparkMax.pidController
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
