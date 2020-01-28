package frc.team4069.saturn.lib.mathematics.model

import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.AngularVelocity
import frc.team4069.saturn.lib.mathematics.units.derived.Torque
import frc.team4069.saturn.lib.mathematics.units.derived.Volt

/**
 * A basic DC motor model
 */
data class DCMotor(val nominalVoltage: SIUnit<Volt>, val stallTorque: SIUnit<Torque>,
              val stallCurrent: SIUnit<Ampere>, val freeCurrent: SIUnit<Ampere>,
              val freeSpeed: SIUnit<AngularVelocity>) {

    /**
     * The resistance of the motor in ohms
     */
    val R = nominalVoltage / stallCurrent

    /**
     * The speed constant of the motor in rad/s/V
     */
    val Kv = freeSpeed / (nominalVoltage - R * freeCurrent)

    /**
     * The torque constant of the motor in Nm/A
     */
    val Kt = stallTorque / stallCurrent
}

val kMotorCim = DCMotor(12.volt, 2.42.joule, 133.amp, 2.7.amp, 5310.rpm)
val kMotorMiniCim = DCMotor(12.volt, 1.41.joule, 89.amp, 3.amp, 5840.rpm)
val kMotorBag = DCMotor(12.volt, 0.43.joule, 53.amp, 1.8.amp, 13180.rpm)
val kMotor775Pro = DCMotor(12.volt, 0.71.joule, 134.amp, 0.7.amp, 18730.rpm)
val kMotorNEO = DCMotor(12.volt, 3.36.joule, 166.amp, 1.3.amp, 5880.rpm)
val kMotorFalcon500 = DCMotor(12.volt, 4.69.joule, 257.amp, 1.5.amp, 6380.rpm)

//TODO: Find a better way to do this?
fun gearbox(motor: DCMotor, numMotors: Int): DCMotor {
    return DCMotor(
            motor.nominalVoltage,
            motor.stallTorque * numMotors.toDouble(),
            motor.stallCurrent,
            motor.freeCurrent,
            motor.freeSpeed
    )
}
