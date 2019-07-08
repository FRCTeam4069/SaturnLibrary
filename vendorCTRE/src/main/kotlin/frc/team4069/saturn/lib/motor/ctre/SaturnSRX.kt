package frc.team4069.saturn.lib.motor.ctre

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.amp
import frc.team4069.saturn.lib.mathematics.units.conversions.millisecond
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel
import kotlin.properties.Delegates

class SaturnSRX<T: Key>(
        val talon: TalonSRX,
        model: NativeUnitModel<T>
) : SaturnCTRE<T>(talon, model) {
    constructor(id: Int, model: NativeUnitModel<T>) : this(TalonSRX(id), model)

    init {
        talon.configFactoryDefault()
    }

    var feedbackSensor by Delegates.observable(FeedbackDevice.QuadEncoder) { _, _, newValue ->
        talon.configSelectedFeedbackSensor(newValue, 0, 0)
    }

    fun configCurrentLimit(enabled: Boolean, config: CurrentLimitConfig) {
        talon.enableCurrentLimit(enabled)
        if (enabled) {
            talon.configPeakCurrentLimit(config.peakCurrentLimit.amp.toInt())
            talon.configPeakCurrentDuration(config.peakCurrentLimitDuration.millisecond.toInt())
            talon.configContinuousCurrentLimit(config.continuousCurrentLimit.amp.toInt())
        }
    }

    data class CurrentLimitConfig(
            val peakCurrentLimit: SIUnit<Ampere>,
            val peakCurrentLimitDuration: Time,
            val continuousCurrentLimit: SIUnit<Ampere>
    )
}
