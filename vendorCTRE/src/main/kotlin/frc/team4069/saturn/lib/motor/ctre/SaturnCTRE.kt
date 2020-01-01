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

package frc.team4069.saturn.lib.motor.ctre

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.IMotorController
import com.ctre.phoenix.motorcontrol.NeutralMode
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.derived.Acceleration
import frc.team4069.saturn.lib.mathematics.units.derived.Velocity
import frc.team4069.saturn.lib.mathematics.units.derived.Volt
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STUPer100ms
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STUPer100msPerSecond
import frc.team4069.saturn.lib.motor.AbstractSaturnMotor
import frc.team4069.saturn.lib.motor.SaturnMotor
import kotlin.properties.Delegates

abstract class SaturnCTRE<T : Key>(
        val motorController: IMotorController,
        val model: NativeUnitModel<T>
) : AbstractSaturnMotor<T>() {

    private var lastDemand =
            Demand(ControlMode.Disabled, 0.0, DemandType.Neutral, 0.0)

    private var compVoltage = 12.volt

    override val encoder = SaturnCTREEncoder(motorController, 0, model)

    override val voltageOutput: SIUnit<Volt>
        get() = motorController.motorOutputVoltage.volt

    override var outputInverted: Boolean by Delegates.observable(false) { _, _, newValue ->
        motorController.inverted = newValue
    }

    override var brakeMode: Boolean by Delegates.observable(false) { _, _, newValue ->
        motorController.setNeutralMode(if (newValue) NeutralMode.Brake else NeutralMode.Coast)
    }

    override var voltageCompSaturation: SIUnit<Volt> by Delegates.observable(12.volt) { _, _, newValue ->
        motorController.configVoltageCompSaturation(newValue.value, 0)
        motorController.enableVoltageCompensation(true)
    }

    override var motionProfileCruiseVelocity: SIUnit<Velocity<T>> by Delegates.observable(model.zero.velocity) { _, _, newValue ->
        motorController.configMotionCruiseVelocity(model.toNativeUnitVelocity(newValue).STUPer100ms.toInt(), 0)
    }
    override var motionProfileAcceleration: SIUnit<Acceleration<T>> by Delegates.observable(model.zero.acceleration) { _, _, newValue ->
        motorController.configMotionAcceleration(model.toNativeUnitAcceleration(newValue).STUPer100msPerSecond.toInt(), 0)

    }

    init {
        motorController.configVoltageCompSaturation(12.0, 0)
        motorController.enableVoltageCompensation(true)
    }

    override fun setVoltage(voltage: SIUnit<Volt>, arbitraryFeedForward: SIUnit<Volt>) =
            sendDemand(
                    Demand(
                            ControlMode.PercentOutput, voltage / compVoltage,
                            DemandType.ArbitraryFeedForward, arbitraryFeedForward / compVoltage
                    )
            )

    override fun setDutyCycle(dutyCycle: Double, arbitraryFeedForward: SIUnit<Volt>) =
            sendDemand(
                    Demand(
                            ControlMode.PercentOutput, dutyCycle,
                            DemandType.ArbitraryFeedForward, arbitraryFeedForward / compVoltage
                    )
            )

    override fun setVelocity(velocity: SIUnit<Velocity<T>>, arbitraryFeedForward: SIUnit<Volt>) =
            sendDemand(
                    Demand(
                            ControlMode.Velocity, model.toNativeUnitVelocity(velocity).STUPer100ms,
                            DemandType.ArbitraryFeedForward, arbitraryFeedForward / compVoltage
                    )
            )

    override fun setPosition(position: SIUnit<T>, arbitraryFeedForward: SIUnit<Volt>) =
            sendDemand(
                    Demand(
                            if (useMotionProfileForPosition) ControlMode.MotionMagic else ControlMode.Position,
                            model.toNativeUnitPosition(position).value,
                            DemandType.ArbitraryFeedForward, arbitraryFeedForward / compVoltage
                    )
            )

    override fun setNeutral() = sendDemand(
            Demand(
                    ControlMode.Disabled,
                    0.0,
                    DemandType.Neutral,
                    0.0
            )
    )

    fun sendDemand(demand: Demand) {
        if (demand != lastDemand) {
            motorController.set(demand.mode, demand.demand0, demand.demand1Type, demand.demand1)
            lastDemand = demand
        }
    }

    override fun follow(motor: SaturnMotor<*>): Boolean =
            if (motor is SaturnCTRE<*>) {
                motorController.follow(motor.motorController)
                true
            } else {
                super.follow(motor)
            }

    data class Demand(
            val mode: ControlMode,
            val demand0: Double,
            val demand1Type: DemandType,
            val demand1: Double
    )
}