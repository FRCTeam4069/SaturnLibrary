package frc.team4069.saturn.lib.util

import kotlin.math.max
import kotlin.math.min

fun Double.safeRangeTo(endInclusive: Double) = min(this, endInclusive)..max(this, endInclusive)

// Eagerly evaluated deadband
fun Double.deadband(threshold: Double) = if(this < threshold) 0.0 else this
