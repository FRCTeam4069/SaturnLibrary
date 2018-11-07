package frc.team4069.saturn.lib.mathematics.onedim.control

interface IKinematicController {
    fun getVelocity(nanotime: Long = System.nanoTime()): PVAData
}