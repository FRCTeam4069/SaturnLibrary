package frc.team4069.saturn.lib.mathematics.onedim.geometry

import frc.team4069.saturn.lib.mathematics.kEpsilon
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.meter
import frc.team4069.saturn.lib.types.VaryInterpolatable
import java.text.DecimalFormat

class Displacement1d internal constructor(internal val _x: Double) : VaryInterpolatable<Displacement1d> {

    constructor() : this(0.0)
    constructor(x: Length) : this(x.meter)

    val x: Length
        get() = _x.meter

    operator fun plus(other: Displacement1d) = Displacement1d(x + other.x)

    override fun distance(other: Displacement1d): Double {
        return other._x - this._x
    }

    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is Displacement1d) false else distance(other) < kEpsilon
    }

    override fun toString(): String {
        val fmt = DecimalFormat("#0.000")
        return "(" + fmt.format(x) + ")"
    }

    fun toCSV(): String {
        val fmt = DecimalFormat("#0.000")
        return fmt.format(x)
    }

    override fun interpolate(upperVal: Displacement1d, interpolatePoint: Double): Displacement1d {
        return this
    }
}