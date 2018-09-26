package frc.team4069.saturn.lib.util

typealias BooleanSource = Source<Boolean>

/**
 * Represents something that acts as a source of the desired value [T]
 */
interface Source<T> {
    val value: T
}