package frc.team4069.saturn.lib.motor

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import frc.team4069.saturn.lib.util.LowPassFilter

/**
 * Standard Talon SRX motor
 */
class SaturnSRX(id: Int,
                private val encoderTicksPerRotation: Int = 4096,
                reversed: Boolean = false,
                val filter: LowPassFilter? = null,
                vararg slaveIds: Int) : WPI_TalonSRX(id) {

    /**
     * The talons set to act as followers to this talon
     */
    private val slaves = mutableSetOf<SaturnSRX>()

    init {
        // Invert our sensor if told
        if(reversed) {
            inverted = true
        }

        // Set up follower motors
        for(slaveId in slaveIds) {
            val slave = SaturnSRX(slaveId, encoderTicksPerRotation,
                    reversed, filter = filter)
            slaves.add(slave)
        }

        slaves.forEach {
            it.follow(this)
        }
    }

    /**
     * Main function to set the output value of this talon
     *
     * in the case of [ControlMode.PercentOutput], the values is clamped to the range -1..1
     */
    override fun set(mode: ControlMode, value: Double) {
        when(mode) {
            ControlMode.PercentOutput -> {
                val clampedValue = value.coerceIn(-1.0..1.0)
                val outValue = filter?.calculate(clampedValue) ?: clampedValue
                super.set(mode, outValue)
            }
            else -> super.set(mode, value)
        }
    }

    var speed: Double
        get() = this.get()
        set(value) {
            set(ControlMode.PercentOutput, value)
        }

    /**
     * Disables the output of the talon
     */
    fun stop() = neutralOutput()

    /**
     * Returns true if the talon is running ([ControlMode] is not [ControlMode.Disabled]), false otherwise
     */
    val isStarted: Boolean get() = controlMode != ControlMode.Disabled

    /**
     * Returns the rotations this talon has traveled, according to the attached encoder
     */
    val distanceTraveledRotations: Double
        get() {
            val quadPos = getSelectedSensorPosition(0)
            return quadPos / encoderTicksPerRotation.toDouble()
        }

    var invertSensorPhase = false
        set(value) {
            field = value
            setSensorPhase(value)
        }

    var motionAcceleration = -1
        set(value) {
            field = value
            configMotionAcceleration(value, 0)
        }

    var motionCruiseVelocity = -1
        set(value) {
            field = value
            configMotionCruiseVelocity(value, 0)
        }

    val position: Int
        get() = getSelectedSensorPosition(0)

    /**
     * Returns the distance traveled in ticks, according to the attached encoder
     */
    val distanceTraveledTicks: Int get() = getSelectedSensorPosition(0)


    /**
     * PID variables here
     */
    var kF = 0.0
        set(value) {
            field = value
            config_kF(0, value, 0)
        }

    var kP = 0.0
        set(value) {
            field = value
            config_kP(0, value, 0)
        }

    var kI = 0.0
        set(value) {
            field = value
            config_kI(0, value, 0)
        }

    var kD = 0.0
        set(value) {
            field = value
            config_kD(0, value, 0)
        }
}
