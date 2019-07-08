package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.units.*

interface IKinematicController {
    fun getVelocity(currentTime: SIUnit<Second> = System.currentTimeMillis().milli.second): PVAData
}