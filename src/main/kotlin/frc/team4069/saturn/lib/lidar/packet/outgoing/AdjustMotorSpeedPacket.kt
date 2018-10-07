package frc.team4069.saturn.lib.lidar.packet.outgoing

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.OutgoingPacket

data class AdjustMotorSpeedPacket(val speed: MotorSpeed) : OutgoingPacket(ControlCode.ADJ_MOTOR_SPEED) {

    override val body = speed.code

    enum class MotorSpeed(val code: String) {
        OFF("00"),
        ONE("01"),
        TWO("02"),
        THREE("03"),
        FOUR("04"),
        FIVE("05"),
        SIX("06"),
        SEVEN("07"),
        EIGHT("08"),
        NINE("09"),
        FASTEST("10"),
    }
}