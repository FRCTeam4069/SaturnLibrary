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
