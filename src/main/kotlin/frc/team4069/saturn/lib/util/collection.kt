package frc.team4069.saturn.lib.util

fun <E> Collection<E>.containsAny(other: Collection<E>): Boolean {
    return this.any { other.contains(it) }
}