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
        val value = source.value
        val newValue = value >= threshold
        if(lastValue != newValue) {
            if(newValue) {
                println("Changing on")
                changeOn.forEach { println("Calling $it"); it() }
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