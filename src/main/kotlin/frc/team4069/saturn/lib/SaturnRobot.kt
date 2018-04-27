package frc.team4069.saturn.lib

import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.command.Scheduler

abstract class SaturnRobot : IterativeRobot() {
    protected val scheduler: Scheduler by lazy {
        Scheduler.getInstance()
    }

}
