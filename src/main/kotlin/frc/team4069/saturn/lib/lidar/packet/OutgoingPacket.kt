package frc.team4069.saturn.lib.lidar.packet

import frc.team4069.saturn.lib.lidar.ControlCode

abstract class OutgoingPacket(val controlCode: ControlCode) {
    val returnSize = controlCode.retSize

    fun serialize() = "${controlCode.asciiCode}$body\n"

    abstract val body: String
}