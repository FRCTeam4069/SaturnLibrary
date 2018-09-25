package frc.team4069.saturn.lib.hid

typealias HIDControlListener = suspend () -> Unit

interface HIDControl {
    suspend fun update()
}

class HIDButton(
    private val source: HIDSource,
    private val threshold: Double,
    private val whileOff: List<HIDControlListener>,
    private val whileOn: List<HIDControlListener>,
    private val changeOn: List<HIDControlListener>,
    private val changeOff: List<HIDControlListener>
) : HIDControl {
    private var lastValue = source.value >= threshold

    override suspend fun update() {
        val newValue = source.value >= threshold

        if (lastValue != newValue) {
            // Change listeners
            if (newValue) {
                changeOn.forEach { it() }
            } else {
                changeOff.forEach { it() }
            }
        } else {
            // While listeners
            if (newValue) {
                whileOn.forEach { it() }
            } else {
                whileOff.forEach { it() }
            }
        }
        lastValue = newValue
    }

    companion object {
        const val DEFAULT_THRESHOLD = 0.5
    }
}
