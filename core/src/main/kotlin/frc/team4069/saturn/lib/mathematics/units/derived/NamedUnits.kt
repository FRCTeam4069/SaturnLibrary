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

package frc.team4069.saturn.lib.mathematics.units.derived

import frc.team4069.saturn.lib.mathematics.units.*

typealias Velocity<T> = Fraction<T, Second>
typealias Acceleration<T> = Fraction<Velocity<T>, Second>

typealias Hertz = Fraction<Unitless, Second>
typealias Curvature = Fraction<Unitless, Meter>

typealias Coulomb = Mult<Ampere, Second>
typealias Volt = Fraction<Joule, Coulomb>
typealias Ohm = Fraction<Volt, Ampere>

typealias Newton = Mult<Kilogram, Acceleration<Meter>>
typealias Joule = Mult<Newton, Meter>
typealias Torque = Mult<Newton, Meter> // Dimensionally identical to energy, but different typealias to enforce it isn't energy
typealias Watt = Fraction<Joule, Second>

typealias VelocityFeedforward = Fraction<Volt, Velocity<Meter>>
typealias AccelerationFeedforward = Fraction<Volt, Acceleration<Meter>>
