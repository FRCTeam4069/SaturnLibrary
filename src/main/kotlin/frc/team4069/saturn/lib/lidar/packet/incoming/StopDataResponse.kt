package frc.team4069.saturn.lib.lidar.packet.incoming

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.InvalidChecksumException
import frc.team4069.saturn.lib.lidar.calculateChecksum
import frc.team4069.saturn.lib.lidar.packet.IncomingPacket

// status is a String because they don't define it
data class StopDataResponse(val status: String) : IncomingPacket(ControlCode.STOP_DATA_ACQ) {

    companion object {
        fun decode(payload: String): StopDataResponse {
            if (payload.substring(0..1) != "DX") {
                throw IllegalArgumentException("Invalid DX packet: $payload")
            }

            val statusSum = calculateChecksum(payload.substring(2..3).toByteArray())

            if (payload[4].toInt() != statusSum) {
                throw InvalidChecksumException(payload[4].toInt(), statusSum)
            }

            return StopDataResponse(payload.substring(2..3))
        }
    }
}