package frc.team4069.saturn.lib.types

interface VaryInterpolatable<S> : Interpolatable<S> {
    fun distance(other: S): Double
}