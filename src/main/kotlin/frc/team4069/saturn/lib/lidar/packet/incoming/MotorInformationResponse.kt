package frc.team4069.saturn.lib.lidar.packet.incoming

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.IncomingPacket

data class MotorInformationResponse(val speedCode: String) : IncomingPacket(ControlCode.MOTOR_SPEED_INFO) {

    companion object {
        fun decode(payload: String): MotorInformationResponse {
            if (payload.substring(0..1) != "MI") {
                throw IllegalArgumentException("Invalid MI packet: $payload")
            }

            return MotorInformationResponse(payload.substring(2..3))
        }
    }
}