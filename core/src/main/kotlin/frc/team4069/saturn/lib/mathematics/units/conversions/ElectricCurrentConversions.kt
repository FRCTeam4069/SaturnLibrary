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

package frc.team4069.saturn.lib.mathematics.units.conversions

import frc.team4069.saturn.lib.mathematics.units.Ampere
import frc.team4069.saturn.lib.mathematics.units.SIConstants
import frc.team4069.saturn.lib.mathematics.units.SIUnit

val SIUnit<Ampere>.yottaamp get() = value / SIConstants.kYotta
val SIUnit<Ampere>.zettaamp get() = value / SIConstants.kZetta
val SIUnit<Ampere>.exaamp get() = value / SIConstants.kExa
val SIUnit<Ampere>.petaamp get() = value / SIConstants.kPeta
val SIUnit<Ampere>.teraamp get() = value / SIConstants.kTera
val SIUnit<Ampere>.gigaamp get() = value / SIConstants.kGiga
val SIUnit<Ampere>.megaamp get() = value / SIConstants.kMega
val SIUnit<Ampere>.kiloamp get() = value / SIConstants.kKilo
val SIUnit<Ampere>.hectoamp get() = value / SIConstants.kHecto
val SIUnit<Ampere>.decaamp get() = value / SIConstants.kDeca
val SIUnit<Ampere>.amp get() = value
val SIUnit<Ampere>.deciamp get() = value / SIConstants.kDeci
val SIUnit<Ampere>.centiamp get() = value / SIConstants.kCenti
val SIUnit<Ampere>.milliamp get() = value / SIConstants.kMilli
val SIUnit<Ampere>.microamp get() = value / SIConstants.kMicro
val SIUnit<Ampere>.nanoamp get() = value / SIConstants.kNano
val SIUnit<Ampere>.picoamp get() = value / SIConstants.kPico
val SIUnit<Ampere>.femtoamp get() = value / SIConstants.kFemto
val SIUnit<Ampere>.attoamp get() = value / SIConstants.kAtto
val SIUnit<Ampere>.zeptoamp get() = value / SIConstants.kZepto
val SIUnit<Ampere>.yoctoamp get() = value / SIConstants.kYocto
