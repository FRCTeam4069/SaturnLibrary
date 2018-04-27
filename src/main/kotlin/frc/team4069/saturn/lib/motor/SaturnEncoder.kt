package frc.team4069.saturn.lib.motor

import edu.wpi.first.wpilibj.Encoder

/**
 * Small wrapper around wpilib [Encoder]
 */
class SaturnEncoder(private val encoderTicksPerRotation: Int = 256,
                    port1: Int,
                    port2: Int) : Encoder(port1, port2) {

    /**
     * Gets the rotations that have been traveled by this encoder
     */
    val distanceTraveledRotations: Double
        get() {
            val quadPosition = get()
            return quadPosition / encoderTicksPerRotation.toDouble()
        }
}