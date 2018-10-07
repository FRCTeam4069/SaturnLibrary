package frc.team4069.saturn.lib.lidar.packet.outgoing

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.OutgoingPacket

object ResetDevicePacket : OutgoingPacket(ControlCode.RESET_DEVICE) {
    override val body = ""
}