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

import com.ctre.phoenix.motorcontrol.IMotorController
import frc.team4069.saturn.lib.mathematics.units.Key
import frc.team4069.saturn.lib.mathematics.units.NativeUnit
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.nativeunits.*
import frc.team4069.saturn.lib.motor.AbstractSaturnEncoder
import kotlin.properties.Delegates

class SaturnCTREEncoder<T: Key>(
        val motorController: IMotorController,
        val pidIdx: Int = 0,
        model: NativeUnitModel<T>
) : AbstractSaturnEncoder<T>(model) {
    override val rawVelocity: SIUnit<NativeUnitVelocity>
        get() = motorController.getSelectedSensorVelocity(pidIdx).STUPer100ms

    override val rawPosition: SIUnit<NativeUnit>
        get() = motorController.getSelectedSensorPosition(pidIdx).STU

    var encoderPhase by Delegates.observable(false) { _, _, newValue ->
        motorController.setSensorPhase(newValue)
    }

    override fun resetPosition(newPosition: SIUnit<T>) {
        motorController.setSelectedSensorPosition(model.toNativeUnitPosition(newPosition).value.toInt(), pidIdx, 0)
    }
}