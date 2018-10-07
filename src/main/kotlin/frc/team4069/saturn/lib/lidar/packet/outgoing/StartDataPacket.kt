package frc.team4069.saturn.lib.lidar.packet.outgoing

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.OutgoingPacket

object StartDataPacket : OutgoingPacket(ControlCode.START_DATA_ACQ) {
    override val body = ""
}