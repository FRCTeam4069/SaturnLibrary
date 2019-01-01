package frc.team4069.saturn.lib.util

import kotlin.reflect.KProperty

open class ReadOnlyObservableValue<T>(protected open var value: T) {
    protected val listeners = mutableListOf<(T) -> Unit>()

    fun get() = value

    fun addListener(listener: (T) -> Unit) {
        listeners.add(listener)
    }

    fun addEntryListener(value: T, callback: () -> Unit) = addListener { newValue ->
        if (newValue == value) callback()
    }

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T = get()
}


class ObservableValue<T>(override var value: T) : ReadOnlyObservableValue<T>(value) {
    fun set(new: T) {
        listeners.forEach { it(new) }
        value = new
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: T) {
        set(value)
    }
}
