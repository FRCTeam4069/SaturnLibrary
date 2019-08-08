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

import com.revrobotics.CANEncoder
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.minute
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitVelocity
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STU
import frc.team4069.saturn.lib.motor.AbstractSaturnEncoder

class SaturnMAXEncoder<T: Key>(
        val canEncoder: CANEncoder,
        model: NativeUnitModel<T>
) : AbstractSaturnEncoder<T>(model) {
    override val rawVelocity: SIUnit<NativeUnitVelocity>
        get() = canEncoder.velocity.STU / 1.minute // RPM

    override val rawPosition: SIUnit<NativeUnit>
        get() = canEncoder.position.STU

    override fun resetPosition(newPosition: SIUnit<T>) {
        canEncoder.position = model.toNativeUnitPosition(newPosition).value
    }

}