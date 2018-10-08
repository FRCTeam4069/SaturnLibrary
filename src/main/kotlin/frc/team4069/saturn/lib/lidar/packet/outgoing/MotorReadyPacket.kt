package frc.team4069.saturn.lib.lidar.packet.outgoing

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.OutgoingPacket

object MotorReadyPacket : OutgoingPacket(ControlCode.MOTOR_READY) {
    override val body = ""
}