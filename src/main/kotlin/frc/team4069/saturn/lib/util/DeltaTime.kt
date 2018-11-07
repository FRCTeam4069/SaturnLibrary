package frc.team4069.saturn.lib.util

import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.second

class DeltaTime(startTime: Time = (-1).second) {
    var deltaTime = 0.second
        private set
    var currentTime = startTime
        private set

    fun updateTime(newTime: Time): Time {
        deltaTime = if (currentTime.value < 0.0) {
            0.second
        } else {
            newTime - currentTime
        }
        currentTime = newTime
        return deltaTime
    }

    fun reset() {
        currentTime = (-1).second
    }
}