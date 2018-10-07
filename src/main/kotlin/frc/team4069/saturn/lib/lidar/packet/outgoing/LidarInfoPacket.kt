package frc.team4069.saturn.lib.lidar.packet.outgoing

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.OutgoingPacket

object LidarInfoPacket : OutgoingPacket(ControlCode.SAMPLE_RATE_INFO) {
    override val body = ""
}
