package frc.team4069.saturn.lib.mathematics.onedim.control

import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearAcceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.meter
import frc.team4069.saturn.lib.util.DeltaTime
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sign

class TrapezoidalProfile private constructor(
        val distance: Double,
        maxVelocity: Double,
        val maxAcceleration: Double,
        val initialX: Double
) : IKinematicController {

    constructor(distance: Length, maxVelocity: LinearVelocity, maxAcceleration: LinearAcceleration,
                initialX: Length = 0.meter)
            : this(distance.meter, maxVelocity.value, maxAcceleration.value, initialX.meter)

    private var cruiseVelocity = maxVelocity

    private val tAccel: Double
    private val xAccel: Double

    private val tCruise: Double
    private val xCruise: Double

    private val sign: Double = sign(distance - initialX)

    init {
        val distance = abs(distance - initialX)
        tAccel = (maxVelocity / maxAcceleration).let {
            if (x(it, 0.0, 0.0, maxAcceleration) < distance / 2) {
                it
            } else {
                cruiseVelocity = maxAcceleration * Math.sqrt(distance / maxAcceleration)
                cruiseVelocity / maxAcceleration
            }
        }

        xAccel = x(tAccel, 0.0, 0.0, maxAcceleration)

        // cruiseVelocity is potentially modified above, this stops a division by zero edge case
        tCruise = if(cruiseVelocity != 0.0) {
            ((distance - (2 * xAccel)) / cruiseVelocity).coerceAtLeast(0.0)
        } else 0.0
        xCruise = x(tCruise, 0.0, cruiseVelocity, 0.0)
    }

    val t1 = tAccel
    val t2 = tAccel + tCruise
    val t3 = 2 * tAccel + tCruise

    // Loops
    private var elapsed = 0.0

    private val deltaTime = DeltaTime()

    override fun getVelocity(time: Time): PVAData {
        val dt = deltaTime.updateTime(time).second
        elapsed += dt

        return when {
            elapsed < t1 -> {
                val t = elapsed
                PVAData(
                        x(t, initialX, 0.0, sign * maxAcceleration),
                        v(t, 0.0, sign * maxAcceleration),
                        sign * maxAcceleration)
            }
            elapsed < t2 -> {
                val t = elapsed - t1
                PVAData(
                        x(t, sign * xAccel + initialX, sign * cruiseVelocity, 0.0),
                        v(t, sign * cruiseVelocity, 0.0),
                        0.0
                )
            }
            elapsed < t3 -> {
                val t = elapsed - t2
                PVAData(
                        x(t, sign * (xAccel + xCruise) + initialX, sign * cruiseVelocity, sign * -maxAcceleration),
                        v(t, sign * cruiseVelocity, sign * -maxAcceleration),
                        sign * -maxAcceleration
                )
            }
            else -> PVAData(distance, 0.0, 0.0)
        }
    }

    private fun x(t: Double, x0: Double, v0: Double, a: Double) = x0 + v0 * t + 0.5 * a * t.pow(2)
    private fun v(t: Double, v0: Double, a: Double) = v0 + a * t
}