package frc.team4069.saturn.lib.util

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

fun launchFrequency(frequency: Int, ctx: CoroutineContext, block: suspend () -> Unit): Job {

    return launch(ctx) {
        val delayTime = 1000 / frequency // Delay (in ms)

        while(isActive) {
            block()
            delay(delayTime)
        }
    }
}