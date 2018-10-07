package frc.team4069.saturn.lib.lidar.packet.incoming

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.IncomingPacket

data class DeviceInfoResponse(
    val bitRate: Int,
    val laserState: Int,
    val mode: Int,
    val diagnostic: Int,
    val motorSpeed: String,
    val sampleRate: String
) : IncomingPacket(ControlCode.DEVICE_INFO) {
    companion object {
        fun decode(payload: String): DeviceInfoResponse {
            if (payload.substring(0..1) != "ID") {
                throw IllegalArgumentException("Invalid ID packet: $payload")
            }

            val bitRate = payload.substring(2..7).toInt()
            val laserState = payload[8].toString().toInt()
            val mode = payload[9].toString().toInt()
            val diagnostic = payload[10].toString().toInt()
            val motorSpeed = payload.substring(11..12)
            val sampleRate = payload.substring(13..16)

            return DeviceInfoResponse(bitRate, laserState, mode, diagnostic, motorSpeed, sampleRate)
        }
    }
}