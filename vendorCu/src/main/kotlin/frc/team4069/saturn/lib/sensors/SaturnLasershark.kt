package frc.team4069.saturn.lib.sensors

import com.cuforge.libcu.Lasershark
import edu.wpi.first.wpilibj.DigitalInput
import frc.team4069.saturn.lib.mathematics.units.Meter
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.centi

class SaturnLasershark(val sensor: Lasershark) {
    constructor(pin: Int) : this(Lasershark(DigitalInput(pin)))
    constructor(digitalInput: DigitalInput) : this(Lasershark(digitalInput))

    val distance: SIUnit<Meter>
        get() = sensor.distanceCentimeters.centi.meter
}


