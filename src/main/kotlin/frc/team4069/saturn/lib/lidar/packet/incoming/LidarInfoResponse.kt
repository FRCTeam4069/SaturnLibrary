package frc.team4069.saturn.lib.lidar.packet.incoming

import frc.team4069.saturn.lib.lidar.ControlCode
import frc.team4069.saturn.lib.lidar.packet.IncomingPacket
import frc.team4069.saturn.lib.lidar.packet.outgoing.AdjustSampleRatePacket

data class LidarInfoResponse(val speed: AdjustSampleRatePacket.SampleRate) :
    IncomingPacket(ControlCode.SAMPLE_RATE_INFO) {

    companion object {
        fun decode(payload: String): LidarInfoResponse {
            if (payload.substring(0..1) != "LI") {
                throw IllegalArgumentException("Invalid LI packet: $payload")
            }

            val speed = AdjustSampleRatePacket.SampleRate.fromCode(payload.substring(2..3))

            return LidarInfoResponse(speed)
        }
    }
}