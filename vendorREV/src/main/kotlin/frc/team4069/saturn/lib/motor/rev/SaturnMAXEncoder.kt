package frc.team4069.saturn.lib.motor.rev

import com.revrobotics.CANEncoder
import frc.team4069.saturn.lib.mathematics.units.Key
import frc.team4069.saturn.lib.mathematics.units.NativeUnit
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.conversions.minute
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitVelocity
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STU
import frc.team4069.saturn.lib.motor.AbstractSaturnEncoder

class SaturnMAXEncoder<T: Key>(
        val canEncoder: CANEncoder,
        model: NativeUnitModel<T>
) : AbstractSaturnEncoder<T>(model) {
    override val rawVelocity: NativeUnitVelocity
        get() = canEncoder.velocity.STU / 1.minute // RPM

    override val rawPosition: SIUnit<NativeUnit>
        get() = canEncoder.position.STU

    override fun resetPosition(newPosition: SIUnit<T>) {
        canEncoder.position = model.toNativeUnitPosition(newPosition).value
    }

}