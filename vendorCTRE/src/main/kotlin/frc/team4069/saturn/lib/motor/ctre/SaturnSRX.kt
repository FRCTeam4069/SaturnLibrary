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

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.amp
import frc.team4069.saturn.lib.mathematics.units.conversions.millisecond
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import kotlin.properties.Delegates

class SaturnSRX<T: Key>(
        val talon: TalonSRX,
        model: NativeUnitModel<T>
) : SaturnCTRE<T>(talon, model) {
    constructor(id: Int, model: NativeUnitModel<T>) : this(TalonSRX(id), model)

    init {
        talon.configFactoryDefault()
    }

    var feedbackSensor by Delegates.observable(FeedbackDevice.QuadEncoder) { _, _, newValue ->
        talon.configSelectedFeedbackSensor(newValue, 0, 0)
    }

    fun configCurrentLimit(enabled: Boolean, config: CurrentLimitConfig) {
        talon.enableCurrentLimit(enabled)
        if (enabled) {
            talon.configPeakCurrentLimit(config.peakCurrentLimit.amp.toInt())
            talon.configPeakCurrentDuration(config.peakCurrentLimitDuration.millisecond.toInt())
            talon.configContinuousCurrentLimit(config.continuousCurrentLimit.amp.toInt())
        }
    }

    data class CurrentLimitConfig(
            val peakCurrentLimit: SIUnit<Ampere>,
            val peakCurrentLimitDuration: SIUnit<Second>,
            val continuousCurrentLimit: SIUnit<Ampere>
    )
}
