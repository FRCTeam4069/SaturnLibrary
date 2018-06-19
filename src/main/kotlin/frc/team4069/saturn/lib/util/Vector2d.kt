package frc.team4069.saturn.lib.util

import kotlin.math.sqrt

// Immutable for the time being, maybe change
data class Vector2d(val x: Double, val y: Double) {
    operator fun plus(other: Vector2d): Vector2d {
        return Vector2d(
                this.x + other.x,
                this.y + other.y
        )
    }

    operator fun minus(other: Vector2d): Vector2d {
        return Vector2d(
                this.x - other.x,
                this.y - other.y
        )
    }

    operator fun times(scalar: Double): Vector2d {
        return Vector2d(
                scalar * this.x,
                scalar * this.y
        )
    }

    // Dot product
    operator fun times(other: Vector2d): Double {
        return  this.x * other.x + this.y * other.y
    }

    fun length() = sqrt(x * x + y * y)

    fun normalize(): Vector2d {
        val len = length()
        return Vector2d(x / len, y / len)
    }
}