package frc.team4069.saturn.lib.commands

import edu.wpi.first.wpilibj.experimental.command.Subsystem
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.util.launchFrequency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job

class SaturnNotifierCommand(private val period: Time,
                            vararg subsystems: Subsystem,
                            private val block: suspend CoroutineScope.() -> Unit) : SaturnCommand(*subsystems) {
    private lateinit var job: Job

    override fun initialize() {
        job = GlobalScope.launchFrequency(period.invert(), block = block)
    }

    override fun end(interrupted: Boolean) {
        job.cancel()
    }
}
