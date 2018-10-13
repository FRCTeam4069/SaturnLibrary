package frc.team4069.saturn.lib.motor

import edu.wpi.first.wpilibj.CounterBase
import edu.wpi.first.wpilibj.Encoder
import kotlin.properties.Delegates

/**
 * Small wrapper around wpilib [Encoder]
 */
class SaturnEncoder(
    val encoderTicksPerRotation: Int = 256,
    port1: Int,
    port2: Int,
    reversed: Boolean = false,
    encodingType: CounterBase.EncodingType = CounterBase.EncodingType.k4X
) : Encoder(port1, port2, reversed, encodingType) {

    var reversed by Delegates.observable(reversed) { _, _, new ->
        setReverseDirection(new)
    }

    /**
     * Gets the rotations that have been traveled by this encoder
     */
    val distanceTraveledRotations: Double
        get() {
            val quadPosition = get()
            return quadPosition / encoderTicksPerRotation.toDouble()
        }
}