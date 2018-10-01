package frc.team4069.saturn.lib.math.geometry

data class Twist2d(val dx: Double = 0.0, val dy: Double = 0.0, val dtheta: Double = 0.0) {
    fun scaled(scale: Double): Twist2d {
        return Twist2d(dx * scale, dy * scale, dtheta * scale)
    }

    fun norm(): Double {
        // Common case of dy == 0
        return if (dy == 0.0) Math.abs(dx) else Math.hypot(dx, dy)
    }
}