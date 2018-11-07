package frc.team4069.saturn.lib.mathematics.units.derivedunits

/* ktlint-disable no-wildcard-imports */
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.fractions.SIFrac33
import frc.team4069.saturn.lib.mathematics.units.fractions.SIFrac34
import frc.team4069.saturn.lib.mathematics.units.fractions.SIFrac35

val Number.volt: Volt
    get() = (this.kilogram * 1.meter * 1.meter) / (1.second * 1.second * 1.second * 1.amp)

typealias Volt = SIFrac34<Mass, Length, Length,
        Time, Time, Time, ElectricCurrent>

typealias Watt = SIFrac33<Mass, Length, Length,
        Time, Time, Time>

typealias Ohm = SIFrac35<Mass, Length, Length,
        Time, Time, Time, ElectricCurrent, ElectricCurrent>