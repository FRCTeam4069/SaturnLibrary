package frc.team4069.saturn.lib.command.builtins

import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.Condition
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import java.util.concurrent.TimeUnit

open class TimeoutCommand(private val timeout: Long, private val unit: TimeUnit = TimeUnit.SECONDS) : Command() {


    private val timeoutCondition = TimeoutCondition()

    init {
        updateFrequency = 0
        finishCondition += timeoutCondition
    }

    override suspend fun initialize() {
        timeoutCondition.start()
    }

    override suspend fun dispose() {
        timeoutCondition.stop()
    }

    private inner class TimeoutCondition : Condition() {
        private lateinit var job: Job
        private var startTime = 0L

        fun start() {
            startTime = System.nanoTime()
            job = launch(timeoutContext) {
                delay(timeout, unit)
                invokeCompletionListeners()
            }
        }

        fun stop() {
            job.cancel()
        }

        override suspend fun isMet() = System.nanoTime() - startTime >= unit.toNanos(timeout)
    }

    companion object {
        private val timeoutContext = newSingleThreadContext("Timeout Command")
    }
}
