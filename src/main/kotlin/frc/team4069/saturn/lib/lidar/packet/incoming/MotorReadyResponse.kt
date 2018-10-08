package frc.team4069.saturn.lib.lidar.packet.incoming

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.IncomingPacket

data class MotorReadyResponse(val ready: Boolean) : IncomingPacket(ControlCode.MOTOR_READY) {

    companion object {
        fun decode(payload: String): MotorReadyResponse {
            if (payload.substring(0..1) != "MZ") {
                throw IllegalArgumentException("Invalid MZ packet: $payload")
            }

            val ready = when (payload.substring(2..3)) {
                "00" -> true
                "01" -> false
                else -> throw IllegalArgumentException("Invalid MZ value")
            }

            return MotorReadyResponse(ready)
        }
    }
}