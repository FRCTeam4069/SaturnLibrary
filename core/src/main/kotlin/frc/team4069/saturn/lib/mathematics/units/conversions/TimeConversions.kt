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

import frc.team4069.saturn.lib.mathematics.units.SIConstants
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.Second

object SITimeConstants {
    const val kMinuteToSecond = 60
    const val kHourToSecond = kMinuteToSecond * 60
    const val kDayToSecond = kHourToSecond * 24
    const val kWeekToSecond = kDayToSecond * 7
    const val kMomentToSecond = 90
}

val Double.minute
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kMinuteToSecond
    )
val Float.minute
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kMinuteToSecond
    )
val Int.minute
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kMinuteToSecond
    )

val Double.hour
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kHourToSecond
    )
val Float.hour
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kHourToSecond
    )
val Int.hour
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kHourToSecond
    )

val Double.day
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kDayToSecond
    )
val Float.day
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kDayToSecond
    )
val Int.day
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kDayToSecond
    )

val Double.week
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kWeekToSecond
    )
val Float.week
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kWeekToSecond
    )
val Int.week
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kWeekToSecond
    )

val Double.moment
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kMomentToSecond
    )
val Float.moment
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kMomentToSecond
    )
val Int.moment
    get() = SIUnit<Second>(
            toDouble() * SITimeConstants.kMomentToSecond
    )

val SIUnit<Second>.minute get() = value / SITimeConstants.kMinuteToSecond
val SIUnit<Second>.hour get() = value / SITimeConstants.kHourToSecond
val SIUnit<Second>.day get() = value / SITimeConstants.kDayToSecond
val SIUnit<Second>.week get() = value / SITimeConstants.kWeekToSecond
val SIUnit<Second>.moment get() = value / SITimeConstants.kMomentToSecond

val SIUnit<Second>.yottasecond get() = value / SIConstants.kYotta
val SIUnit<Second>.zettasecond get() = value / SIConstants.kZetta
val SIUnit<Second>.exasecond get() = value / SIConstants.kExa
val SIUnit<Second>.petasecond get() = value / SIConstants.kPeta
val SIUnit<Second>.terasecond get() = value / SIConstants.kTera
val SIUnit<Second>.gigasecond get() = value / SIConstants.kGiga
val SIUnit<Second>.megasecond get() = value / SIConstants.kMega
val SIUnit<Second>.kilosecond get() = value / SIConstants.kKilo
val SIUnit<Second>.hectosecond get() = value / SIConstants.kHecto
val SIUnit<Second>.decasecond get() = value / SIConstants.kDeca
val SIUnit<Second>.second get() = value
val SIUnit<Second>.decisecond get() = value / SIConstants.kDeci
val SIUnit<Second>.centisecond get() = value / SIConstants.kCenti
val SIUnit<Second>.millisecond get() = value / SIConstants.kMilli
val SIUnit<Second>.microsecond get() = value / SIConstants.kMicro
val SIUnit<Second>.nanosecond get() = value / SIConstants.kNano
val SIUnit<Second>.picosecond get() = value / SIConstants.kPico
val SIUnit<Second>.femtosecond get() = value / SIConstants.kFemto
val SIUnit<Second>.attosecond get() = value / SIConstants.kAtto
val SIUnit<Second>.zeptosecond get() = value / SIConstants.kZepto
val SIUnit<Second>.yoctosecond get() = value / SIConstants.kYocto
