package frc.team4069.saturn.lib.lidar.packet.outgoing

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.OutgoingPacket

data class AdjustSampleRatePacket(val newRate: SampleRate) : OutgoingPacket(ControlCode.ADJ_SAMPLE_RATE) {

    override val body = newRate.code

    enum class SampleRate(val code: String) {
        DEFAULT("01"),
        FASTER("02"),
        FASTEST("03");

        companion object {
            fun fromCode(code: String) = when(code) {
                "01" -> SampleRate.DEFAULT
                "02" -> SampleRate.FASTER
                "03" -> SampleRate.FASTEST
                else -> throw IllegalArgumentException("Invalid sample rate code: $code")
            }
        }
    }
}