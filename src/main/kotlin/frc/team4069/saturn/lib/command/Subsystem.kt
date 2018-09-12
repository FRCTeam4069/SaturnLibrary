package frc.team4069.saturn.lib.command

/**
 * Represents a physical subsystem on the robot
 */
abstract class Subsystem {
    /**
     * The command that will be run when nothing else is running that requires this subsystem
     */
    open val defaultCommand: Command? = null

    /**
     * The frequency at which [periodic] is called, in hertz
      */
    open val updateFrequency = 50

    open suspend fun periodic() {

    }
}