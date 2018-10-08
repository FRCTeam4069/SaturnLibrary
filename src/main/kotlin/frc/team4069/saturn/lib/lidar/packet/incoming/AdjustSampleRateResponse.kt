package frc.team4069.saturn.lib.lidar.packet.incoming

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.InvalidChecksumException
import frc.team4069.saturn.lib.lidar.calculateChecksum
import frc.team4069.saturn.lib.lidar.packet.IncomingPacket
import frc.team4069.saturn.lib.lidar.packet.outgoing.AdjustSampleRatePacket

data class AdjustSampleRateResponse(
    val sampleRate: AdjustSampleRatePacket.SampleRate,
    val status: Status
) : IncomingPacket(ControlCode.ADJ_SAMPLE_RATE) {
    enum class Status {
        OK,
        INVALID_PARAM;

        companion object {
            fun fromCode(code: String) = when (code) {
                "00" -> Status.OK
                "11" -> Status.INVALID_PARAM
                else -> throw IllegalArgumentException("Invalid code for LR status $code")
            }
        }
    }

    companion object {
        fun decode(payload: String): AdjustSampleRateResponse {
            if (payload.substring(0..1) != "LR") {
                throw IllegalArgumentException("Invalid LR packet: $payload")
            }

            val sampleRate = AdjustSampleRatePacket.SampleRate.fromCode(payload.substring(2..3))

            val sum = calculateChecksum(payload.substring(5..6).toByteArray())
            if (sum != payload[7].toInt()) {
                throw InvalidChecksumException(payload[7].toInt(), sum)
            }

            val status = Status.fromCode(payload.substring(5..6))

            return AdjustSampleRateResponse(sampleRate, status)
        }
    }
}