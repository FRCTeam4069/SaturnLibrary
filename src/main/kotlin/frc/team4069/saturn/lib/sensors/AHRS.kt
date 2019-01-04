package frc.team4069.saturn.lib.sensors

<<<<<<< HEAD
import com.ctre.phoenix.sensors.PigeonIMU
=======
>>>>>>> master
import frc.team4069.saturn.lib.mathematics.units.Rotation2d
import frc.team4069.saturn.lib.mathematics.units.degree

interface IAHRSSensor {
    var angleOffset: Rotation2d
    val correctedAngle: Rotation2d
    fun reset()
}

abstract class AHRSSensor : IAHRSSensor {
    protected abstract val yaw: Rotation2d
    override var angleOffset = 0.degree
    override val correctedAngle:Rotation2d get() = yaw + angleOffset
}

<<<<<<< HEAD
class Pigeon(val pigeon: PigeonIMU) : AHRSSensor() {

    override val yaw: Rotation2d
        get() = pigeon.fusedHeading.degree

    override fun reset() {
        pigeon.setYaw(0.0, 0)
    }

}

fun PigeonIMU.toSaturn() = Pigeon(this)
=======
//class Pigeon(val pigeon: PigeonIMU) : AHRSSensor() {
//
//    override val yaw: Rotation2d
//        get() = pigeon.fusedHeading.degree
//
//    override fun reset() {
//        pigeon.setYaw(0.0, 0)
//    }
//
//}
//
//fun PigeonIMU.toSaturn() = Pigeon(this)
>>>>>>> master
