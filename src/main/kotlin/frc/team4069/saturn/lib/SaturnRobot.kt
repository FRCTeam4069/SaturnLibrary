package frc.team4069.saturn.lib

import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.command.Scheduler
import frc.team4069.saturn.lib.util.BrownoutWatchdog

abstract class SaturnRobot : IterativeRobot() {
    val brownoutWatchdog = BrownoutWatchdog(::notifyBrownout)

    override fun robotPeriodic() {
        Scheduler.getInstance().run()
//        brownoutWatchdog.feed()
    }

    open fun notifyBrownout() {}
}

