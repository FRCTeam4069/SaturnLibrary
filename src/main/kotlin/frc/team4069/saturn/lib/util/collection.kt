package frc.team4069.saturn.lib.util

import java.util.*

fun <E> Collection<E>.containsAny(other: Collection<E>): Boolean {
    return this.any { other.contains(it) }
}

fun <E> LinkedList<E>.removeOrNull(): E? = if (this.peek() != null) {
    this.remove()
} else {
    null
}

