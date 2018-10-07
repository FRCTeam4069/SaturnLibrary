package frc.team4069.saturn.lib.lidar.packet.incoming

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.InvalidChecksumException
import frc.team4069.saturn.lib.lidar.calculateChecksum
import frc.team4069.saturn.lib.lidar.packet.IncomingPacket

data class StartDataResponse(val status: Status) : IncomingPacket(ControlCode.START_DATA_ACQ) {
    enum class Status(val code: String) {
        OK("00"),
        MOT_UNSTABLE("12"),
        MOT_STATIONARY("13");

        companion object {
            fun fromCode(code: String) = when (code) {
                "00" -> Status.OK
                "12" -> Status.MOT_UNSTABLE
                "13" -> Status.MOT_STATIONARY
                else -> throw IllegalArgumentException("Invalid DS status: $code")
            }
        }
    }

    companion object {
        fun decode(payload: String): StartDataResponse {
            if (payload.substring(0..1) != "DS") {
                throw IllegalArgumentException("$payload not a valid DS packet")
            }


            val statusSum = calculateChecksum(payload.substring(2..3).toByteArray())

            if (payload[4].toInt() != statusSum) {
                throw InvalidChecksumException(payload[4].toInt(), statusSum)
            }

            return StartDataResponse(Status.fromCode(payload.substring(2..3)))
        }
    }
}


