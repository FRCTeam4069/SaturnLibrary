package frc.team4069.saturn.lib.utils

import kotlin.math.max
import kotlin.math.min

fun Double.safeRangeTo(endInclusive: Double) = min(this, endInclusive)..max(this, endInclusive)