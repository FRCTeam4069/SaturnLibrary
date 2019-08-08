package frc.team4069.saturn.lib.localization

import edu.wpi.first.wpilibj.Timer
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.conversions.second
import frc.team4069.saturn.lib.mathematics.units.second
import frc.team4069.saturn.lib.types.Interpolatable
import frc.team4069.saturn.lib.util.Source
import java.util.*

class TimeInterpolatableBuffer<T : Interpolatable<T>>(
        historySpan: Time = 1.second,
        private val timeSource: Source<Time> = { Timer.getFPGATimestamp().second }
) {

    private val historySpan = historySpan.second
    private val bufferMap = TreeMap<Double, T>()

    operator fun set(time: Time, value: T) = set(time.second, value)

    internal operator fun set(time: Double, value: T): T? {
        cleanUp()
        return bufferMap.put(time, value)
    }

    private fun cleanUp() {
        val currentTime = timeSource().second
        val iterator = bufferMap.iterator()
        iterator.forEach {
            if (currentTime - it.key >= historySpan) {
                iterator.remove()
            }
        }
    }

    fun clear() {
        bufferMap.clear()
    }

    operator fun get(time: Time) = get(time.second)

    internal operator fun get(time: Double): T {
        bufferMap[time]?.let { return it }

        val topBound = bufferMap.ceilingEntry(time)
        val bottomBound = bufferMap.floorEntry(time)

        return when {
            topBound == null -> bottomBound.value
            bottomBound == null -> topBound.value
            else -> bottomBound.value.interpolate(
                topBound.value,
                (time - bottomBound.key) / (topBound.key - bottomBound.key)
            )
        }
    }
}