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

/**
 * Contains extra utils for converting to and from length units
 */
package frc.team4069.saturn.lib.mathematics.units.conversions

import frc.team4069.saturn.lib.mathematics.units.Meter
import frc.team4069.saturn.lib.mathematics.units.SIConstants
import frc.team4069.saturn.lib.mathematics.units.SIUnit

object SILengthConstants {
    const val kInchToMeter = 0.0254
    const val kThouToMeter = kInchToMeter * 0.001
    const val kLineToMeter = kInchToMeter * (1.0 / 12.0)
    const val kFeetToMeter = kInchToMeter * 12
    const val kYardToMeter = kFeetToMeter * 3
    const val kMileToMeter = kFeetToMeter * 5280
    const val kLeagueToMeter = kMileToMeter * 3
    const val kNauticalMile = 1852
    const val kLightYearToMeter = 9460730472580800.0
}

val Double.thou
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kThouToMeter
    )
val Float.thou
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kThouToMeter
    )
val Int.thou
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kThouToMeter
    )

val Double.line
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kLineToMeter
    )
val Float.line
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kLineToMeter
    )
val Int.line
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kLineToMeter
    )

val Double.inch
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kInchToMeter
    )
val Float.inch
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kInchToMeter
    )
val Int.inch
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kInchToMeter
    )

val Double.feet
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kFeetToMeter
    )
val Float.feet
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kFeetToMeter
    )
val Int.feet
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kFeetToMeter
    )

val Double.yard
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kYardToMeter
    )
val Float.yard
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kYardToMeter
    )
val Int.yard
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kYardToMeter
    )

val Double.mile
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kMileToMeter
    )
val Float.mile
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kMileToMeter
    )
val Int.mile
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kMileToMeter
    )

val Double.league
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kLeagueToMeter
    )
val Float.league
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kLeagueToMeter
    )
val Int.league
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kLeagueToMeter
    )

val Double.nauticalMile
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kNauticalMile
    )
val Float.nauticalMile
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kNauticalMile
    )
val Int.nauticalMile
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kNauticalMile
    )

val Double.lightYear
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kLightYearToMeter
    )
val Float.lightYear
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kLightYearToMeter
    )
val Int.lightYear
    get() = SIUnit<Meter>(
            toDouble() * SILengthConstants.kLightYearToMeter
    )

val SIUnit<Meter>.thou get() = value / SILengthConstants.kThouToMeter
val SIUnit<Meter>.line get() = value / SILengthConstants.kLineToMeter
val SIUnit<Meter>.inch get() = value / SILengthConstants.kInchToMeter
val SIUnit<Meter>.feet get() = value / SILengthConstants.kFeetToMeter
val SIUnit<Meter>.yard get() = value / SILengthConstants.kYardToMeter
val SIUnit<Meter>.mile get() = value / SILengthConstants.kMileToMeter
val SIUnit<Meter>.league get() = value / SILengthConstants.kLeagueToMeter
val SIUnit<Meter>.nauticalMile get() = value / SILengthConstants.kNauticalMile
val SIUnit<Meter>.lightYear get() = value / SILengthConstants.kLightYearToMeter

val SIUnit<Meter>.yottameter get() = value / SIConstants.kYotta
val SIUnit<Meter>.zettameter get() = value / SIConstants.kZetta
val SIUnit<Meter>.exameter get() = value / SIConstants.kExa
val SIUnit<Meter>.petameter get() = value / SIConstants.kPeta
val SIUnit<Meter>.terameter get() = value / SIConstants.kTera
val SIUnit<Meter>.gigameter get() = value / SIConstants.kGiga
val SIUnit<Meter>.megameter get() = value / SIConstants.kMega
val SIUnit<Meter>.kilometer get() = value / SIConstants.kKilo
val SIUnit<Meter>.hectometer get() = value / SIConstants.kHecto
val SIUnit<Meter>.decameter get() = value / SIConstants.kDeca
val SIUnit<Meter>.meter get() = value
val SIUnit<Meter>.decimeter get() = value / SIConstants.kDeci
val SIUnit<Meter>.centimeter get() = value / SIConstants.kCenti
val SIUnit<Meter>.millimeter get() = value / SIConstants.kMilli
val SIUnit<Meter>.micrometer get() = value / SIConstants.kMicro
val SIUnit<Meter>.nanometer get() = value / SIConstants.kNano
val SIUnit<Meter>.picometer get() = value / SIConstants.kPico
val SIUnit<Meter>.femtometer get() = value / SIConstants.kFemto
val SIUnit<Meter>.attometer get() = value / SIConstants.kAtto
val SIUnit<Meter>.zeptometer get() = value / SIConstants.kZepto
val SIUnit<Meter>.yoctometer get() = value / SIConstants.kYocto
