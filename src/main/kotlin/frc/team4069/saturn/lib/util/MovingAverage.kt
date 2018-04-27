package frc.team4069.saturn.lib.util

class MovingAverage(private val size: Int) {
    internal var backing = Array(size, { 0.0 })
    var idx = 0

    fun add(element: Double) {
        backing[idx++ % size] = element
        idx %= size
    }

    fun clear() {
        for(i in 0 until size) {
            backing[i] = 0.0
        }
    }

    val average: Double get() = backing.average()
}
