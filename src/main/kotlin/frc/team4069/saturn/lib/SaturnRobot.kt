package frc.team4069.saturn.lib

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import frc.team4069.saturn.lib.hid.SaturnHID
import frc.team4069.saturn.lib.util.BrownoutWatchdog

//const val kLanguageKotlin = 6

abstract class SaturnRobot : TimedRobot() {
    private val brownoutWatchdog = BrownoutWatchdog(::notifyBrownout)
    private val controls = mutableListOf<SaturnHID<*>>()

    // If we actually want unique reporting for kotlin, use this
//    override fun robotInit() {
//        HAL.report(FRCNetComm.tResourceType.kResourceType_Language, kLanguageKotlin)
//    }

    override fun robotPeriodic() {
        Scheduler.getInstance().run()
        brownoutWatchdog.feed()
        controls.forEach { it.update() }
    }

    override fun autonomousInit() {
        Shuffleboard.startRecording()
    }

    override fun teleopInit() {
        Shuffleboard.startRecording()
    }

    override fun disabledInit() {
        Shuffleboard.stopRecording()
    }

    open fun notifyBrownout() {}

    protected operator fun SaturnHID<*>.unaryPlus() = controls.add(this)
}

