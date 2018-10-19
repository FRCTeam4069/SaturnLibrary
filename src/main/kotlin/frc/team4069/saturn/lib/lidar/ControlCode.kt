package frc.team4069.saturn.lib.lidar

/**
 * Represents the possible control codes for communication with a Scanse Sweep
 */
enum class ControlCode(val asciiCode: String, val retSize: Int) {
    /**
     * Instructs the Sweep to start acquiring data
     */
    START_DATA_ACQ("DS", 6),

    /**
     * Instructs the Sweep to stop acquiring data
     */
    STOP_DATA_ACQ("DX", 6),

    /**
     * Sent by the Sweep when the motor is ready
     */
    MOTOR_READY("MZ", 5),

    /**
     * Instructs the Sweep to change the motor speed
     */
    ADJ_MOTOR_SPEED("MS", 9),

    /**
     * Asks for the motor speed info from the Sweep
     */
    MOTOR_SPEED_INFO("MI", 5),

    /**
     * Instructs the Sweep to change the sample rate of the LiDAR
     */
    ADJ_SAMPLE_RATE("LR", 9),

    /**
     * Asks for sample rate info from the Sweep
     */
    SAMPLE_RATE_INFO("LI", 5),

    /**
     * Asks for version info from the Sweep
     */
    VERSION_INFO("IV", 21),

    /**
     * Asks for device info from the Sweep
     */
    DEVICE_INFO("ID", 18),

    /**
     * Instructs the Sweep to reset itself
     */
    RESET_DEVICE("RR", -1);

    companion object {
        fun fromAscii(code: String): ControlCode = when (code) {
            "DS" -> ControlCode.START_DATA_ACQ
            "DX" -> ControlCode.STOP_DATA_ACQ
            "MZ" -> ControlCode.MOTOR_READY
            "MS" -> ControlCode.ADJ_MOTOR_SPEED
            "MI" -> ControlCode.MOTOR_SPEED_INFO
            "LR" -> ControlCode.ADJ_SAMPLE_RATE
            "LI" -> ControlCode.SAMPLE_RATE_INFO
            "IV" -> ControlCode.VERSION_INFO
            "ID" -> ControlCode.DEVICE_INFO
            "RR" -> ControlCode.RESET_DEVICE
            else -> throw IllegalArgumentException("Invalid control code $code")
        }
    }
}