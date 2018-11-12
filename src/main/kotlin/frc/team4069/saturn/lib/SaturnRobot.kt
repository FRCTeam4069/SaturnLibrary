package frc.team4069.saturn.lib

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Scheduler
import frc.team4069.saturn.lib.hid.SaturnHID
import frc.team4069.saturn.lib.util.BrownoutWatchdog

abstract class SaturnRobot : TimedRobot() {
    private val brownoutWatchdog = BrownoutWatchdog(::notifyBrownout)
    private
    val controls = mutableListOf<SaturnHID<*>>()

    override fun robotPeriodic() {
        Scheduler.getInstance().run()
        brownoutWatchdog.feed()
        controls.forEach { it.update() }
    }

    open fun notifyBrownout() {}

    protected operator fun SaturnHID<*>.unaryPlus() = controls.add(this)
}

