package frc.team4069.saturn.lib.util

/**
 * Basic implementation of a multi map data structure, where for any key K, there may be many values V associated with it.
 * Used internally in [StateMachine] to store callbacks against their desired state.
 */
class MultiMap<K, V>(private val map: MutableMap<K, MutableSet<V>>) : MutableMap<K, MutableSet<V>> by map {
    constructor() : this(mutableMapOf())

    fun putSingle(key: K, value: V) {

        map[key] = (map[key] ?: mutableSetOf()).apply { add(value) }
    }

    override fun put(key: K, value: MutableSet<V>): MutableSet<V>? {
        value.forEach { putSingle(key, it) }
        return null
    }

    fun containsSingleValue(value: V): Boolean = map.values.any { it.contains(value) }
}
