package frc.team4069.saturn.lib.lidar

class InvalidChecksumException(expected: Int, actual: Int) :
    Exception("Checksums differ. Expected: $expected. Actual: $actual")
