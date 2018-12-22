package frc.team4069.saturn.lib.motor

/**
 * Standard Talon SRX motor
 */
//@Deprecated("Replaced with TypedSaturnSRX", ReplaceWith("TypedSaturnSRX"))
//class SaturnSRX(id: Int,
//                private val encoderTicksPerRotation: Int = 4096,
//                reversed: Boolean = false,
//                private val filter: LowPassFilter? = null,
//                vararg slaveIds: Int) : WPI_TalonSRX(id) {
//
//    /**
//     * The talons set to act as followers to this talon
//     */
//    private val slaves = mutableSetOf<SaturnSRX>()
//
//    init {
//        // Invert our sensor if told
//        if(reversed) {
//            inverted = true
//        }
//
//        // Set up follower motors
//        slaveIds.mapTo(slaves) {
//            SaturnSRX(it, encoderTicksPerRotation,
//                    reversed, filter = filter)
//        }
//
//        slaves.forEach {
//            it.follow(this)
//        }
//    }
//
//    /**
//     * Main function to set the output value of this talon
//     *
//     * in the case of [ControlMode.PercentOutput], the values is clamped to the range -1..1
//     */
//    override fun set(mode: ControlMode, value: Double) {
//        when(mode) {
//            ControlMode.PercentOutput -> {
//                val clampedValue = value.coerceIn(-1.0..1.0)
//                super.set(mode, clampedValue)
//            }
//            else -> super.set(mode, value)
//        }
//    }
//
//    var speed: Double
//        get() = this.get()
//        set(value) {
//            set(ControlMode.PercentOutput, value)
//        }
//
//    /**
//     * Disables the output of the talon
//     */
//    fun stop() = neutralOutput()
//
//    /**
//     * Returns true if the talon is running ([ControlMode] is not [ControlMode.Disabled]), false otherwise
//     */
//    val isStarted: Boolean get() = controlMode != ControlMode.Disabled
//
//    /**
//     * Returns the rotations this talon has traveled, according to the attached encoder
//     */
//    val distanceTraveledRotations: Double
//        get() {
//            val quadPos = getSelectedSensorPosition(0)
//            return quadPos / encoderTicksPerRotation.toDouble()
//        }
//
//    var invertSensorPhase by Delegates.observable(false) { _, _, value ->
//        setSensorPhase(value)
//    }
//
//    var motionAcceleration by Delegates.observable(-1) { _, _, value ->
//        configMotionAcceleration(value, 0)
//    }
//
//    var motionCruiseVelocity by Delegates.observable(-1) { _, _, value ->
//        configMotionCruiseVelocity(value, 0)
//    }
//
//    val position: Int
//        get() = getSelectedSensorPosition(0)
//
//
//    var forwardSoftLimitThreshold by Delegates.observable(0) { _, _, value ->
//        configForwardSoftLimitThreshold(value, 0)
//    }
//
//
//    var forwardSoftLimitEnabled by Delegates.observable(false) { _, _, value ->
//        configForwardSoftLimitEnable(value, 0)
//    }
//
//    var reverseSoftLimitThreshold by Delegates.observable(0) { _, _, value ->
//        configReverseSoftLimitThreshold(value, 0)
//    }
//
//
//    var reverseSoftLimitEnabled by Delegates.observable(false) { _, _, value ->
//        configReverseSoftLimitEnable(value, 0)
//    }
//
//    var continuousCurrentLimit by Delegates.observable(0) { _, _, value ->
//        configContinuousCurrentLimit(value, 0)
//    }
//
//    var currentLimitEnabled by Delegates.observable(false) { _, _, value ->
//        enableCurrentLimit(value)
//    }
//
//    var peakCurrentLimit by Delegates.observable(0) { _, _, value ->
//        configPeakCurrentLimit(value, 0)
//    }
//
//    var peakCurrentDuration by Delegates.observable(0) { _, _, value ->
//        configPeakCurrentDuration(value, 0)
//    }
//
//    /**
//     * PID variables here
//     */
//    var f by Delegates.observable(0.0) { _, _, value ->
//        config_kF(0, value, 0)
//    }
//
//
//    var p by Delegates.observable(0.0) { _, _, value ->
//        config_kP(0, value, 0)
//    }
//
//    var i by Delegates.observable(0.0) { _, _, value ->
//        config_kI(0, value, 0)
//    }
//
//    var d by Delegates.observable(0.0) { _, _, value ->
//        config_kD(0, value, 0)
//    }
//}
