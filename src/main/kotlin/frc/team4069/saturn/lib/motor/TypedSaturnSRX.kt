package frc.team4069.saturn.lib.motor

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.volt
import frc.team4069.saturn.lib.mathematics.units.nativeunits.*
import kotlin.properties.Delegates.observable

class TypedSaturnSRX<T : SIUnit<T>>(
    id: Int,
    private val model: NativeUnitModel<T>,
    timeout: Time = 10.millisecond
) : WPI_TalonSRX(id) {
    private val timeoutInt = timeout.millisecond.toInt()

    var kP by observable(0.0) { _, _, newValue -> config_kP(0, newValue, timeoutInt) }
    var kI by observable(0.0) { _, _, newValue -> config_kI(0, newValue, timeoutInt) }
    var kD by observable(0.0) { _, _, newValue -> config_kD(0, newValue, timeoutInt) }
    var kF by observable(0.0) { _, _, newValue -> config_kF(0, newValue, timeoutInt) }
    var encoderPhase by observable(false) { _, _, newValue -> setSensorPhase(newValue) }


    var overrideLimitSwitchesEnable by observable(false) { _, _, newValue ->
        overrideLimitSwitchesEnable(newValue)
    }

    var softLimitForwardEnabled by observable(false) { _, _, newValue ->
        configForwardSoftLimitEnable(newValue, timeoutInt)
    }
    var softLimitReverseEnabled by observable(false) { _, _, newValue ->
        configReverseSoftLimitEnable(newValue, timeoutInt)
    }
    var softLimitForward by observable(0.STU) { _, _, newValue ->
        configForwardSoftLimitThreshold(newValue.value.toInt(), timeoutInt)
    }
    var softLimitReverse by observable(0.STU) { _, _, newValue ->
        configReverseSoftLimitThreshold(newValue.value.toInt(), timeoutInt)
    }

    var brakeMode by observable(NeutralMode.Coast) { _, _, newValue ->
        setNeutralMode(newValue)
    }

    var allowedClosedLoopError by observable(model.zero) { _, _, newValue ->
        configAllowableClosedloopError(0, newValue.fromModel(model).value.toInt(), timeoutInt)
    }

    var nominalForwardOutput by observable(0.0) { _, _, newValue ->
        configNominalOutputForward(newValue, timeoutInt)
    }
    var nominalReverseOutput by observable(0.0) { _, _, newValue ->
        configNominalOutputReverse(newValue, timeoutInt)
    }

    var peakForwardOutput by observable(1.0) { _, _, newValue ->
        configPeakOutputForward(newValue, timeoutInt)
    }
    var peakReverseOutput by observable(-1.0) { _, _, newValue ->
        configPeakOutputReverse(newValue, timeoutInt)
    }

    var openLoopRamp by observable(0.second) { _, _, newValue ->
        configOpenloopRamp(newValue.second, timeoutInt)
    }
    val closedLoopRamp by observable(0.second) { _, _, newValue ->
        configClosedloopRamp(newValue.second, timeoutInt)
    }

    var motionCruiseVelocity by observable(model.zero.velocity) { _, _, newValue ->
        configMotionCruiseVelocity(newValue.fromModel(model).STUPer100ms.toInt(), timeoutInt)
    }

    var motionAcceleration by observable(model.zero.acceleration) { _, _, newValue ->
        configMotionAcceleration(newValue.fromModel(model).STUPer100msPerSecond.toInt(), timeoutInt)
    }

    var feedbackSensor by observable(FeedbackDevice.QuadEncoder) { _, _, newValue ->
        configSelectedFeedbackSensor(newValue, 0, timeoutInt)
    }

    var peakCurrentLimit by observable(0.amp) { _, _, newValue ->
        configPeakCurrentLimit(newValue.amp.toInt(), timeoutInt)
    }
    var peakCurrentLimitDuration by observable(0.millisecond) { _, _, newValue ->
        configPeakCurrentDuration(newValue.millisecond.toInt(), timeoutInt)
    }
    var continuousCurrentLimit by observable(0.amp) { _, _, newValue ->
        configContinuousCurrentLimit(newValue.amp.toInt(), timeoutInt)
    }
    var currentLimitingEnabled by observable(false) { _, _, newValue ->
        enableCurrentLimit(newValue)
    }

    var voltageCompensationSaturation by observable(12.volt) { _, _, newValue ->
        configVoltageCompSaturation(newValue.value, timeoutInt)
    }
    var voltageCompensationEnabled by observable(false) { _, _, newValue ->
        enableVoltageCompensation(newValue)
    }

    var sensorPosition
        get() = getSelectedSensorPosition(0).STU.toModel(model)
        set(pos) {
            setSelectedSensorPosition(model.fromModel(pos).value.toInt(), 0, timeoutInt)
        }

    val sensorVelocity get() = getSelectedSensorVelocity(0).STUPer100ms.toModel(model)

    fun set(controlMode: ControlMode, length: T) = set(controlMode, length.fromModel(model).value)

    fun set(controlMode: ControlMode, velocity: Velocity<T>) = set(controlMode, velocity, DemandType.ArbitraryFeedForward, 0.0)

    fun set(
        controlMode: ControlMode,
        velocity: Velocity<T>,
        demandType: DemandType,
        outputPercent: Double
    ) = set(controlMode, velocity.fromModel(model).value, demandType, outputPercent)
}