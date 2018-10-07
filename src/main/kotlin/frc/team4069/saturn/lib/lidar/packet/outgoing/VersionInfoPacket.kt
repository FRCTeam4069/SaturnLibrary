package frc.team4069.saturn.lib.lidar.packet.outgoing

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.OutgoingPacket


object VersionInfoPacket : OutgoingPacket(ControlCode.VERSION_INFO) {
    override val body = ""
}