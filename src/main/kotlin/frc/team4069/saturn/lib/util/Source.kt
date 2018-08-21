package frc.team4069.saturn.lib.util

typealias BooleanSource = Source<Boolean>

interface Source<T> {
    val value: T
}