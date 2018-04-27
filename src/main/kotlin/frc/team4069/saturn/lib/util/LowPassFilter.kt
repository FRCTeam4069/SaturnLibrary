package frc.team4069.saturn.lib.util

/**
 * Low pass filter implementation with the cutoff frequency based off of a delta time
 */
class LowPassFilter(val rc: Int) {

    var lastTime = -1L
    var lastValue = 0.0

    /**
     * Smooth the [value] based upon delta-time cutoff frequency
      */
    fun calculate(value: Double): Double {
        if(lastTime != -1L) {
            val now = System.currentTimeMillis()

            var dt = now - lastTime.toDouble()

            dt /= dt + rc

            lastTime = now

            lastValue = dt * value + (1 - dt) * lastValue
        }else {
            lastTime = System.currentTimeMillis()
        }

        return lastValue
    }
}