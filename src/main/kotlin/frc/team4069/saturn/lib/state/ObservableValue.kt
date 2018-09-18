package frc.team4069.saturn.lib.state

import kotlin.reflect.KProperty

typealias ObservableListener<T> = ObservableHandle.(T) -> Unit

interface ObservableValue<T> {
    val value: T

    operator fun getValue(ref: Any, prop: KProperty<*>) = value

    fun invokeOnSet(listener: ObservableListener<T>): ObservableHandle

    fun invokeOnSet(vararg choices: T, listener: ObservableListener<T>) = invokeOnSet {
        if (choices.contains(it)) {
            this.listener(it)
        }
    }

    private fun invokeOnce(listener: ObservableListener<T>): ObservableListener<T> = {
        try {
            this.listener(it)
        } finally {
            dispose()
        }
    }

    fun invokeOnceOnSet(listener: ObservableListener<T>) = invokeOnSet(listener = invokeOnce(listener))
    fun invokeOnceOnSet(vararg choices: T, listener: ObservableListener<T>) = invokeOnSet(*choices, listener = invokeOnce(listener))

    fun invokeOnChange(listener: ObservableListener<T>): ObservableHandle {
        var lastValue = value

        return invokeOnSet {
            if(lastValue != it) this.listener(it)
            lastValue = it
        }
    }

    fun invokeOnceOnChange(listener: ObservableListener<T>) = invokeOnChange(invokeOnce(listener))

    fun invokeOnChangeTo(vararg values: T, listener: ObservableListener<T>) = invokeOnChangeTo(values.asList(), listener)

    fun invokeOnChangeTo(values: List<T>, listener: ObservableListener<T>): ObservableHandle {
        var firstRun = true
        var currentValue = value
        return invokeOnSet {
            if (values.contains(it) && (currentValue != it || firstRun)) listener(this, it)
            currentValue = it
            firstRun = false
        }
    }

    // Invoke When

    fun invokeWhen(vararg values: T, listener: ObservableListener<T>) = invokeWhen(values.asList(), listener)

    fun invokeWhen(values: Collection<T>, listener: ObservableListener<T>): ObservableHandle {
        var firstRun = true
        var currentValue = value
        return invokeWhen {
            if (values.contains(it) && (currentValue != it || firstRun)) listener(this, it)
            currentValue = it
            firstRun = false
        }
    }

    fun invokeWhen(listener: ObservableListener<T>): ObservableHandle {
        val syncValue = Any()
        var currentValue = value
        synchronized(syncValue) {
            val handle = invokeOnSet {
                synchronized(syncValue) {
                    if (currentValue != it) listener(this, it)
                    currentValue = it
                }
            }
            currentValue = value
            listener(handle, currentValue)
            return handle
        }
    }

    fun invokeOnceWhen(listener: ObservableListener<T>) = invokeWhen(listener = invokeOnce(listener))
    fun invokeOnceWhen(vararg values: T, listener: ObservableListener<T>) = invokeWhen(*values, listener = invokeOnce(listener))


    companion object {

    }
}
