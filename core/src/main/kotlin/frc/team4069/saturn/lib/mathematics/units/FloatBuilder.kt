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
val Float.yotta get() = UnitBuilder(toDouble() * SIConstants.kYotta)
val Float.zetta get() = UnitBuilder(toDouble() * SIConstants.kZetta)
val Float.exa get() = UnitBuilder(toDouble() * SIConstants.kExa)
val Float.peta get() = UnitBuilder(toDouble() * SIConstants.kPeta)
val Float.tera get() = UnitBuilder(toDouble() * SIConstants.kTera)
val Float.giga get() = UnitBuilder(toDouble() * SIConstants.kGiga)
val Float.mega get() = UnitBuilder(toDouble() * SIConstants.kMega)
val Float.kilo get() = UnitBuilder(toDouble() * SIConstants.kKilo)
val Float.hecto get() = UnitBuilder(toDouble() * SIConstants.kHecto)
val Float.deca get() = UnitBuilder(toDouble() * SIConstants.kDeca)
// unit is omitted because scalar is 1
val Float.deci get() = UnitBuilder(toDouble() * SIConstants.kDeci)
val Float.centi get() = UnitBuilder(toDouble() * SIConstants.kCenti)
val Float.milli get() = UnitBuilder(toDouble() * SIConstants.kMilli)
val Float.micro get() = UnitBuilder(toDouble() * SIConstants.kMicro)
val Float.nano get() = UnitBuilder(toDouble() * SIConstants.kNano)
val Float.pico get() = UnitBuilder(toDouble() * SIConstants.kPico)
val Float.femto get() = UnitBuilder(toDouble() * SIConstants.kFemto)
val Float.atto get() = UnitBuilder(toDouble() * SIConstants.kAtto)
val Float.zepto get() = UnitBuilder(toDouble() * SIConstants.kZepto)
val Float.yocto get() = UnitBuilder(toDouble() * SIConstants.kYocto)



/**
 * Prefixless getters for all defined units.
 */
val Float.gram get() = SIUnit<Kilogram>(
        toDouble() / 1000.0
)
val Float.second get() = SIUnit<Second>(
        toDouble()
)
val Float.amp get() = SIUnit<Ampere>(
        toDouble()
)
val Float.coulomb get() = SIUnit<Coulomb>(
        toDouble()
)
val Float.volt get() = SIUnit<Volt>(
        toDouble()
)
val Float.ohm get() = SIUnit<Ohm>(
        toDouble()
)
val Float.newton get() = SIUnit<Newton>(
        toDouble()
)
val Float.joule get() = SIUnit<Joule>(
        toDouble()
)
val Float.hertz get() = SIUnit<Hertz>(
        toDouble()
)
val Float.curvature get() = SIUnit<Curvature>(
        toDouble()
)
val Float.meter get() = SIUnit<Meter>(
        toDouble()
)

val Float.radian get() = SIUnit<Radian>(
        toDouble()
)
val Float.degree get() = SIUnit<Radian>(
        Math.toRadians(toDouble())
)