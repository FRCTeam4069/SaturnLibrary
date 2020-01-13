package frc.team4069.saturn.lib.shuffleboard.logging

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser

fun <T> sendableChooser(vararg pairs: Pair<String, T>): SendableChooser<T> {
    return SendableChooser<T>().apply {
        for(pair in pairs) {
            this.addOption(pair.first, pair.second)
        }
    }
}