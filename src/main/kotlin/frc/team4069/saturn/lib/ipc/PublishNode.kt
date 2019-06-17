package frc.team4069.saturn.lib.ipc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext

abstract class PublishNode {
    abstract val name: String

    internal val publishChannel = BroadcastChannel<Message>(Channel.CONFLATED)

    fun publish(msg: Message) {
        publishChannel.offer(msg)
    }

    fun subscribe(other: PublishNode) {
        messageHandlingScope.launch {
            other.publishChannel.openSubscription()
                    .consumeEach { handleMessage(it) }
        }
    }

    open fun handleMessage(msg: Message) {}

    companion object {
        val messageHandlingScope = CoroutineScope(newFixedThreadPoolContext(2, "Incoming Message Pool"))
    }
}