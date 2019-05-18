package frc.team4069.saturn.lib.motor.rev

import com.revrobotics.CANEncoder
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.minute
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnit
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitVelocity
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STU
import frc.team4069.saturn.lib.motor.AbstractSaturnEncoder

class SaturnMAXEncoder<T: SIUnit<T>>(
        val canEncoder: CANEncoder,
        model: NativeUnitModel<T>
) : AbstractSaturnEncoder<T>(model) {
    override val rawVelocity: NativeUnitVelocity
        get() = canEncoder.velocity.STU / 1.minute // RPM

    override val rawPosition: NativeUnit
        get() = canEncoder.position.STU

    override fun resetPosition(newPosition: Double) {
        canEncoder.position = newPosition
    }

}