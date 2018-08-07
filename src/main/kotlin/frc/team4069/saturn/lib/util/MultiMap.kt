package frc.team4069.saturn.lib.util

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
