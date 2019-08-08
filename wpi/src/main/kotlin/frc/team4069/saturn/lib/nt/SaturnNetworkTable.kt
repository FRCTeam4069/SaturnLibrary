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

package frc.team4069.saturn.lib.nt

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import kotlin.reflect.KProperty

object SaturnNetworkTable {
    val instance: NetworkTableInstance = NetworkTableInstance.getDefault()

    operator fun get(name: String): NetworkTableEntry = getEntry(name)
    fun getEntry(name: String): NetworkTableEntry = instance.getEntry(name)

    fun getTable(name: String): NetworkTable = instance.getTable(name)
}

operator fun NetworkTable.get(name: String): NetworkTableEntry = getEntry(name)

fun NetworkTableEntry.delegate(defaultValue: String = ""): NetworkTableEntryDelegate<String> =
    delegate { this.getString(defaultValue) }

fun NetworkTableEntry.delegate(defaultValue: Double = 0.0): NetworkTableEntryDelegate<Double> =
    delegate { this.getDouble(defaultValue) }

fun NetworkTableEntry.delegate(defaultValue: Boolean = false): NetworkTableEntryDelegate<Boolean> =
    delegate { this.getBoolean(defaultValue) }

private fun <T> NetworkTableEntry.delegate(get: () -> T) = NetworkTableEntryDelegate(this, get)

class NetworkTableEntryDelegate<T>(
    private val entry: NetworkTableEntry,
    private val get: () -> T
) {
    operator fun setValue(networkInterface: Any, property: KProperty<*>, value: T) {
        entry.setValue(value)
    }

    operator fun getValue(networkInterface: Any, property: KProperty<*>): T = get()
}
