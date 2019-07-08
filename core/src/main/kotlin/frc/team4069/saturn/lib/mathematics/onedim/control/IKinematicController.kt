package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.milli
import frc.team4069.saturn.lib.mathematics.units.second

interface IKinematicController {
    fun getVelocity(currentTime: Time = System.currentTimeMillis().milli.second): PVAData
}