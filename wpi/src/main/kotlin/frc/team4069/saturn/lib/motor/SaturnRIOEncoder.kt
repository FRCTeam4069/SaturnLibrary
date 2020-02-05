package frc.team4069.saturn.lib.motor

import edu.wpi.first.wpilibj.Encoder
import frc.team4069.saturn.lib.mathematics.units.Key
import frc.team4069.saturn.lib.mathematics.units.NativeUnit
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitVelocity
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STU
import frc.team4069.saturn.lib.mathematics.units.velocity

/**
 * A encoder connected directly to the roboRIO digital input ports
 */
class SaturnRIOEncoder<T: Key>(val encoder: Encoder, model: NativeUnitModel<T>) : AbstractSaturnEncoder<T>(model) {
    /**
     * The offset value for the encoder. Since wpilib reset doesn't allow us to arbitrarily set the internal count,
     * an offset is used to store the desired new position of the encoder.
     */
    private var encoderOffset = 0.STU

    override val rawVelocity: SIUnit<NativeUnitVelocity>
        get() = encoder.rate.STU.velocity

    override val rawPosition: SIUnit<NativeUnit>
        get() = encoder.get().STU + encoderOffset

    override fun resetPosition(newPosition: SIUnit<T>) {
        encoder.reset()
        encoderOffset = model.toNativeUnitPosition(newPosition)
    }

}