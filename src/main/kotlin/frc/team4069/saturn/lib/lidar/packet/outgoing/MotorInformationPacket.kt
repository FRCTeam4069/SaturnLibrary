package frc.team4069.saturn.lib.lidar.packet.outgoing

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.OutgoingPacket

object MotorInformationPacket : OutgoingPacket(ControlCode.MOTOR_SPEED_INFO) {
    override val body = ""
}