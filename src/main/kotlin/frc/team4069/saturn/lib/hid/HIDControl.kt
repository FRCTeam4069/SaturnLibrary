package frc.team4069.saturn.lib.hid

typealias HIDListener = suspend () -> Unit

class HIDButton(private val source: HIDSource,
                private val threshold: Double = DEFAULT_THRESHOLD,
                private val changeOn: List<HIDListener>,
                private val changeOff: List<HIDListener>) : HIDControl{
    companion object {
        const val DEFAULT_THRESHOLD = 0.5
    }

    private var lastValue = source.value >= threshold

    override suspend fun update() {
        val newValue = source.value >= threshold
        if(lastValue != newValue) {
            println("The value is changed")
            if(newValue) {
                changeOn.forEach { it() }
            }else {
                changeOff.forEach { it() }
            }
        }

        lastValue = newValue
    }
}

interface HIDControl {
    suspend fun update()
}