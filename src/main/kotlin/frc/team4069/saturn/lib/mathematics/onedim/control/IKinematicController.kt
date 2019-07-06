package frc.team4069.saturn.lib.mathematics.onedim.control

import edu.wpi.first.wpilibj.Timer
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.second

interface IKinematicController {
    fun getVelocity(nanotime: Time = Timer.getFPGATimestamp().second): PVAData
}