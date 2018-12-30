package frc.team4069.saturn.lib.util

class ObservableValue<T>(var value: T) {

    val listeners = mutableListOf<(T) -> Unit>()

    fun set(new: T) {
        listeners.forEach { it(new) }
        value = new
    }


    fun addListener(listener: (T) -> Unit) {
        listeners.add(listener)
    }

    fun addEntryListener(value: T, callback: () -> Unit) = addListener { newValue ->
        if(newValue == value) callback()
    }
}
