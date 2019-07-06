package frc.team4069.saturn.lib.commands

import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.experimental.command.SendableSubsystemBase
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

/**
 *  Kotlin wrapper for [SendableSubsystemBase]
 */
abstract class SaturnSubsystem : SendableSubsystemBase() {
    open fun lateInit() {}
    open fun autoReset() {}
    open fun teleopReset() {}
    open fun setNeutral() {}
}
