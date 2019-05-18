package frc.team4069.saturn.lib.motor

import frc.team4069.saturn.lib.mathematics.units.SIUnit

abstract class AbstractSaturnMotor<T: SIUnit<T>> : SaturnMotor<T> {
    override var useMotionProfileForPosition: Boolean = false

    override fun follow(motor: SaturnMotor<*>): Boolean {
        TODO("Cross brand motor controller following not yet implemented!")
    }
}