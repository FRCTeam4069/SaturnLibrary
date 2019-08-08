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

package frc.team4069.saturn.lib.hid

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.saturn.lib.util.Source

typealias HIDSource = Source<Double>

class HIDButtonSource<T : GenericHID>(val device: T, val buttonId: Int) : HIDSource {
    override fun invoke() = if (device.getRawButton(buttonId)) 1.0 else 0.0
}

class HIDAxisSource<T : GenericHID>(val device: T, val axisId: Int) : HIDSource {
    override fun invoke() = device.getRawAxis(axisId)
}

class HIDPOVSource<T : GenericHID>(val device: T, val povId: Int, val angle: Int) : HIDSource {
    override fun invoke() = if (device.getPOV(povId) == angle) 1.0 else 0.0
}