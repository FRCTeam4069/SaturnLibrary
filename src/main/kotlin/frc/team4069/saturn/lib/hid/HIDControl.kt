package frc.team4069.saturn.lib.hid

typealias HIDControlListener = suspend () -> Unit

/**
 * General interface for an input on a Human Interface Device (HID) that can update its state
 */
interface HIDControl {
    suspend fun update()
}

/**
 * Class representing an input on a HID.
 *
 * Will read from the provided [source], and will call functions in [whileOff], [whileOn], [changeOff], and [changeOn]
 * based on if the input from [source] is greater than [threshold], and based on the last value it received.
 */
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
