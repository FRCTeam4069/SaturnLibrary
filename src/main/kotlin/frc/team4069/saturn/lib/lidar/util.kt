package frc.team4069.saturn.lib.lidar

fun calculateChecksum(status: ByteArray): Int {
    if(status.size != 2) {
        throw IllegalArgumentException("Status array must be 2 bytes")
    }

    return (status.sum() and 0x3F) + 0x30
}

fun calculateDataChecksum(data: ByteArray): Int {
    if(data.size != 7) {
        throw IllegalArgumentException("Data block must be 7 bytes")
    }

    return data.sliceArray(0..5).sum() % 255
}