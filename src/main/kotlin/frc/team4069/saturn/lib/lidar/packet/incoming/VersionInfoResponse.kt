package frc.team4069.saturn.lib.lidar.packet.incoming

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.IncomingPacket

data class VersionInfoResponse(
    val model: String, val protocol: String, val firmwareVersion: String,
    val hardwareVersion: String, val serialNumber: String
) : IncomingPacket(ControlCode.VERSION_INFO) {

    companion object {
        fun decode(payload: String): VersionInfoResponse {
            if (payload.substring(0..1) != "IV") {
                throw IllegalArgumentException("Invalid IV packet: $payload")
            }

            val model = payload.substring(2..6)
            val proto = payload.substring(7..8)
            val firmwareVersion = payload.substring(9..10)
            val hardwareVersion = payload[11].toString()
            val serialNumber = payload.substring(12..20)

            return VersionInfoResponse(model, proto, firmwareVersion, hardwareVersion, serialNumber)
        }
    }
}