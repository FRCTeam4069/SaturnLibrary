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

package frc.team4069.saturn.lib.mathematics.units.nativeunits

import frc.team4069.saturn.lib.mathematics.TAU
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.inch
import frc.team4069.saturn.lib.mathematics.units.derived.Acceleration
import frc.team4069.saturn.lib.mathematics.units.derived.Velocity

class NativeUnitLengthModel(
        sensorUnitsPerRotation: SIUnit<NativeUnit> = kDefaultSensorUnitsPerRotation,
        val wheelRadius: SIUnit<Meter> = 3.inch
) : NativeUnitModel<Meter>(sensorUnitsPerRotation, 0.inch) {
    override fun fromNativeUnitPosition(value: SIUnit<NativeUnit>): SIUnit<Meter> =
            wheelRadius * ((value / sensorUnitsPerRotation) * TAU)

    override fun toNativeUnitPosition(value: SIUnit<Meter>): SIUnit<NativeUnit> =
            sensorUnitsPerRotation * (value / (wheelRadius * TAU))
}

class NativeUnitRotationModel(
        sensorUnitsPerRotation: SIUnit<NativeUnit> = kDefaultSensorUnitsPerRotation
) : NativeUnitModel<Radian>(sensorUnitsPerRotation, 0.degree) {
    override fun fromNativeUnitPosition(value: SIUnit<NativeUnit>) =
            360.degree * (value / sensorUnitsPerRotation)

    override fun toNativeUnitPosition(value: SIUnit<Radian>) =
            sensorUnitsPerRotation * (value / 360.degree)
}

class NativeUnitSensorModel(sensorUnitsPerRotation: SIUnit<NativeUnit> = kDefaultSensorUnitsPerRotation) : NativeUnitModel<NativeUnit>(sensorUnitsPerRotation, 0.STU) {
    override fun fromNativeUnitPosition(value: SIUnit<NativeUnit>): SIUnit<NativeUnit> {
        return value
    }

    override fun toNativeUnitPosition(value: SIUnit<NativeUnit>): SIUnit<NativeUnit> {
        return value
    }
}

abstract class NativeUnitModel<T: Key>(
        val sensorUnitsPerRotation: SIUnit<NativeUnit>,
        val zero: SIUnit<T>
) {
    abstract fun fromNativeUnitPosition(value: SIUnit<NativeUnit>): SIUnit<T>
    abstract fun toNativeUnitPosition(value: SIUnit<T>): SIUnit<NativeUnit>

    fun fromNativeUnitVelocity(value: SIUnit<NativeUnitVelocity>): SIUnit<Velocity<T>> {
        return fromNativeUnitPosition(SIUnit(value.value)).velocity
    }

    fun toNativeUnitVelocity(value: SIUnit<Velocity<T>>): SIUnit<NativeUnitVelocity> {
        return toNativeUnitPosition(SIUnit(value.value)).velocity
    }

    fun fromNativeUnitAcceleration(value: SIUnit<NativeUnitAcceleration>): SIUnit<Acceleration<T>> {
        return fromNativeUnitPosition(SIUnit(value.value)).acceleration
    }

    fun toNativeUnitAcceleration(value: SIUnit<Acceleration<T>>): SIUnit<NativeUnitAcceleration> {
        return toNativeUnitPosition(SIUnit(value.value)).acceleration
    }

    companion object {
        val kDefaultSensorUnitsPerRotation = 4096.STU
    }
}