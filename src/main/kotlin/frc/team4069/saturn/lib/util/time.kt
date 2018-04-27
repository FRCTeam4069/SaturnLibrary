package frc.team4069.saturn.lib.util

import edu.wpi.first.wpilibj.Timer

val systemTimeMillis: Double
    get() = try {
        Timer.getFPGATimestamp() * 1000.0
    }catch(_: Exception) {
        System.currentTimeMillis().toDouble()
    }