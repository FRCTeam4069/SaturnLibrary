package frc.team4069.saturn.lib.sensors

import com.ctre.phoenix.sensors.PigeonIMU
import frc.team4069.saturn.lib.mathematics.units.Rotation2d
import frc.team4069.saturn.lib.mathematics.units.degree
import frc.team4069.saturn.lib.motor.SaturnSRX
import frc.team4069.saturn.lib.util.Source

class SaturnPigeon(parentTalon: SaturnSRX<*>) : PigeonIMU(parentTalon), Source<Rotation2d> {
    val fusedHeading: Rotation2d
        get() = this.getFusedHeading().degree

    val quaternion: Quaternion
        get() {
            val arr = DoubleArray(4)
            get6dQuaternion(arr)

            return Quaternion(arr[0], arr[1], arr[2], arr[3])
        }

    override fun invoke() = fusedHeading

    data class Quaternion(val w: Double, val x: Double, val y: Double, val z: Double)
}