package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.millisecond


interface IKinematicController {
    fun getVelocity(nanotime: Time = System.currentTimeMillis().millisecond): PVAData
}