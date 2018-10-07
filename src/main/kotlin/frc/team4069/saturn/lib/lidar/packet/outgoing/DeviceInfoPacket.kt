package frc.team4069.saturn.lib.lidar.packet.outgoing

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.OutgoingPacket

object DeviceInfoPacket : OutgoingPacket(ControlCode.DEVICE_INFO) {
    override val body = ""
}
