/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
