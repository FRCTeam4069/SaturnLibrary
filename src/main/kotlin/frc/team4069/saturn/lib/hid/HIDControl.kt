package frc.team4069.saturn.lib.hid


class HIDButton(private val source: HIDSource,
                private val threshold: Double,
                private val whileOff: List<HIDControlListener>,
                private val whileOn: List<HIDControlListener>,
                private val changeOn: List<HIDControlListener>,
                private val changeOff: List<HIDControlListener>) : HIDControl {

    companion object {
        const val DEFAULT_THRESHOLD = 0.5
    }

    private var lastValue = source.value >= threshold

    override fun update() {
        val newValue = source.value >= threshold
        if (lastValue != newValue) {
            // Value has changed
            if (newValue) {
                changeOn.forEach { it() }
            } else {
                changeOff.forEach { it() }
            }
        } else {
            // Value stayed the same
            if (newValue) {
                whileOn.forEach { it() }
            } else {
                whileOff.forEach { it() }
            }
        }
        lastValue = newValue
    }
}

typealias HIDControlListener = () -> Unit

interface HIDControl {
    fun update()
}
