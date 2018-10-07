package frc.team4069.saturn.lib.lidar.packet.incoming

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.InvalidChecksumException
import frc.team4069.saturn.lib.lidar.calculateChecksum
import frc.team4069.saturn.lib.lidar.packet.IncomingPacket

data class AdjustMotorSpeedResponse(val newSpeed: String, val status: Status) :
    IncomingPacket(ControlCode.ADJ_MOTOR_SPEED) {
    enum class Status {
        OK,
        INVALID_PARAM,
        MOT_UNSTABLE;

        companion object {
            fun fromCode(code: String) = when (code) {
                "00" -> Status.OK
                "11" -> Status.INVALID_PARAM
                "12" -> Status.MOT_UNSTABLE
                else -> throw IllegalArgumentException("Invalid MS status code $code")
            }
        }
    }

    companion object {
        fun decode(payload: String): AdjustMotorSpeedResponse {
            if (payload.substring(0..1) != "MS") {
                throw IllegalArgumentException("Invalid MS packet: $payload")
            }

            val speedCode = payload.substring(2..3)

            val sum = calculateChecksum(payload.substring(5..6).toByteArray())
            if (sum != payload[7].toInt()) {
                throw InvalidChecksumException(payload[7].toInt(), sum)
            }

            val status = Status.fromCode(payload.substring(5..6))

            return AdjustMotorSpeedResponse(speedCode, status)
        }
    }
}