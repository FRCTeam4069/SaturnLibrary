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

package frc.team4069.saturn.lib.mathematics.units

import frc.team4069.saturn.lib.mathematics.units.derived.*

/**
 * Prefixless getters for SI constants.
 */
val Int.yotta get() = UnitBuilder(toDouble() * SIConstants.kYotta)
val Int.zetta get() = UnitBuilder(toDouble() * SIConstants.kZetta)
val Int.exa get() = UnitBuilder(toDouble() * SIConstants.kExa)
val Int.peta get() = UnitBuilder(toDouble() * SIConstants.kPeta)
val Int.tera get() = UnitBuilder(toDouble() * SIConstants.kTera)
val Int.giga get() = UnitBuilder(toDouble() * SIConstants.kGiga)
val Int.mega get() = UnitBuilder(toDouble() * SIConstants.kMega)
val Int.kilo get() = UnitBuilder(toDouble() * SIConstants.kKilo)
val Int.hecto get() = UnitBuilder(toDouble() * SIConstants.kHecto)
val Int.deca get() = UnitBuilder(toDouble() * SIConstants.kDeca)
// unit is omitted because scalar is 1
val Int.deci get() = UnitBuilder(toDouble() * SIConstants.kDeci)
val Int.centi get() = UnitBuilder(toDouble() * SIConstants.kCenti)
val Int.milli get() = UnitBuilder(toDouble() * SIConstants.kMilli)
val Int.micro get() = UnitBuilder(toDouble() * SIConstants.kMicro)
val Int.nano get() = UnitBuilder(toDouble() * SIConstants.kNano)
val Int.pico get() = UnitBuilder(toDouble() * SIConstants.kPico)
val Int.femto get() = UnitBuilder(toDouble() * SIConstants.kFemto)
val Int.atto get() = UnitBuilder(toDouble() * SIConstants.kAtto)
val Int.zepto get() = UnitBuilder(toDouble() * SIConstants.kZepto)
val Int.yocto get() = UnitBuilder(toDouble() * SIConstants.kYocto)



/**
 * Prefixless getters for all defined units.
 */
val Int.gram get() = SIUnit<Kilogram>(
        toDouble() / 1000.0
)
val Int.second get() = SIUnit<Second>(
        toDouble()
)
val Int.amp get() = SIUnit<Ampere>(
        toDouble()
)
val Int.coulomb get() = SIUnit<Coulomb>(
        toDouble()
)
val Int.volt get() = SIUnit<Volt>(
        toDouble()
)
val Int.ohm get() = SIUnit<Ohm>(
        toDouble()
)
val Int.newton get() = SIUnit<Newton>(
        toDouble()
)
val Int.joule get() = SIUnit<Joule>(
        toDouble()
)
val Int.hertz get() = SIUnit<Hertz>(
        toDouble()
)
val Int.curvature get() = SIUnit<Curvature>(
        toDouble()
)
val Int.meter get() = SIUnit<Meter>(
        toDouble()
)

val Int.radian get() = SIUnit<Unitless>(
        toDouble()
)
val Int.degree get() = SIUnit<Unitless>(
        Math.toRadians(toDouble())
)