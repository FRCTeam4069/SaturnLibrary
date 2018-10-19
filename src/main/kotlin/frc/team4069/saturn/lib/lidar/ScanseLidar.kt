package frc.team4069.saturn.lib.lidar

import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.SerialPort
import frc.team4069.saturn.lib.lidar.packet.IncomingPacket
import frc.team4069.saturn.lib.lidar.packet.OutgoingPacket
import frc.team4069.saturn.lib.lidar.packet.incoming.*
import frc.team4069.saturn.lib.lidar.packet.outgoing.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlin.experimental.and

class ScanseLidar(port: SerialPort.Port) {

    private val conn = SerialPort(115200, port, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne)
    private val dataNotifier = Notifier(::run)
    val pointHistory = mutableListOf<LidarPoint>()

    init {
        conn.disableTermination()
        conn.setFlowControl(SerialPort.FlowControl.kNone)

        conn.setReadBufferSize(32767)
        conn.setWriteBufferMode(SerialPort.WriteBufferMode.kFlushOnAccess)
        conn.setWriteBufferSize(64)

        runBlocking {
            reset()
        }
    }

    suspend fun start() {
        println("Requesting version info")
        val ivRes = sendPacket(VersionInfoPacket) as VersionInfoResponse
        println("Response ok: $ivRes")

        delay(1000)

        val idRes = sendPacket(DeviceInfoPacket) as DeviceInfoResponse
        println("Device information: $idRes")

        if (idRes.motorSpeed != AdjustMotorSpeedPacket.MotorSpeed.FIVE) {
            delay(1500)
            println("Starting motor")
            val res =
                sendPacket(AdjustMotorSpeedPacket(AdjustMotorSpeedPacket.MotorSpeed.FIVE)) as AdjustMotorSpeedResponse
            println("Got res $res")

            delay(4750)
        }

        delay(250)

        while (true) {
            val motorStatus = sendPacket(MotorReadyPacket) as MotorReadyResponse
            if (motorStatus.ready) {
                break
            }

            delay(150)
        }

        delay(500)

        val mi = sendPacket(MotorInformationPacket) as MotorInformationResponse
        println("new MI: $mi")

        delay(500)

        val dsRes = sendPacket(StartDataPacket) as StartDataResponse
        if (dsRes.status == StartDataResponse.Status.OK) {
            dataNotifier.startPeriodic(0.02)
        }
    }

    suspend fun reset() {
        if(pointHistory.isNotEmpty()) {
            pointHistory.clear()
        }
        sendPacket(ResetDevicePacket)
        delay(5000)
    }

    fun run() {
        if (conn.bytesReceived < 7) {
            return
        }

        val buf = conn.read(7)

        val sync = buf[0]
        if ((sync and 2).toInt() != 0) {
            println("COMM FAILURE TO LIDAR")
            dataNotifier.stop()
        }

        if ((sync and 1).toInt() != 0) {
            // maybe do something here
        }

        val angle = ((buf[2].toInt() shl 8) + buf[1]).toDouble() / 16.0
        val dist = (buf[4].toInt() shl 8) + buf[3]
        val ss = buf[5].toInt()

        val sum = calculateDataChecksum(buf)

        if (sum != buf[6].toInt()) {
            // Checksum mismatch
//            println("CHECKSUM MISMATCH: GOT $sum. EXPECTED ${buf[6].toInt()}")
//            runBlocking {
//                reset()
//                dataNotifier.stop()
//            }
            return
        }

        synchronized(pointHistory) {
            pointHistory.add(LidarPoint(angle, dist, ss))
        }
    }

    suspend fun sendPacket(packet: OutgoingPacket): IncomingPacket? {

        val payload = packet.serialize()
        println("Writing $payload")
        conn.writeString(payload)

        return if (packet.controlCode != ControlCode.RESET_DEVICE) {
            println("Trying to decode ${packet.controlCode}")
            val rawPayload = readPacket(packet.returnSize)
            when (ControlCode.fromAscii(rawPayload.substring(0..1))) {
                ControlCode.START_DATA_ACQ -> StartDataResponse.decode(rawPayload)
                ControlCode.STOP_DATA_ACQ -> StopDataResponse.decode(rawPayload)
                ControlCode.MOTOR_READY -> MotorReadyResponse.decode(rawPayload)
                ControlCode.ADJ_MOTOR_SPEED -> AdjustMotorSpeedResponse.decode(rawPayload)
                ControlCode.MOTOR_SPEED_INFO -> MotorInformationResponse.decode(rawPayload)
                ControlCode.ADJ_SAMPLE_RATE -> AdjustSampleRateResponse.decode(rawPayload)
                ControlCode.DEVICE_INFO -> DeviceInfoResponse.decode(rawPayload)
                ControlCode.SAMPLE_RATE_INFO -> LidarInfoResponse.decode(rawPayload)
                ControlCode.VERSION_INFO -> VersionInfoResponse.decode(rawPayload)
                else -> throw IllegalStateException("Unreachable code")
            }
        } else null
    }

    private suspend fun readPacket(size: Int): String {
        while (conn.bytesReceived < size) {
            delay(50)
        }

        return conn.readString(size)
    }
}