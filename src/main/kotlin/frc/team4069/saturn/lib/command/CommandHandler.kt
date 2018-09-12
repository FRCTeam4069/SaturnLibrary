package frc.team4069.saturn.lib.command

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.actor

object CommandHandler {
    val COMMAND_CTX = newFixedThreadPoolContext(4, "Command")

    internal val runningCommands = mutableMapOf<Subsystem, CommandHandle>()

    sealed class Message {
        data class Start(val cmd: Command, val handle: CompletableDeferred<CommandHandle>? = null) : Message()
        data class Stop(val cmd: CommandHandle) : Message()
    }

    val commandActor = actor<Message> {
        while (isActive) {
            for (msg in channel) {
                when (msg) {
                    is Message.Start -> {
                        val handle = startCommand(msg.cmd)
                        msg.handle?.complete(handle)
                    }
                    is Message.Stop -> stopCommand(msg.cmd)
                }
            }
        }
    }

    private suspend fun startCommand(cmd: Command): CommandHandle {
        val commandsToStop = runningCommands.filterKeys { it in cmd.requiredSubsystems }

        commandsToStop.forEach { (subsystem, cmd) ->
                    stopCommand(cmd)
                    runningCommands.remove(subsystem)
                }

        val task = CommandHandle(cmd)
        cmd.requiredSubsystems.forEach { runningCommands[it] = task }

        return task
    }

    private suspend fun stopCommand(cmd: CommandHandle) {
        cmd.handle.cancelAndJoin()
        cmd.inner.requiredSubsystems.mapNotNull(Subsystem::defaultCommand)
                .map { Message.Start(it) }
                .forEach { commandActor.send(it) }
        when (cmd.state) {
            CommandHandle.State.FINISHED -> cmd.inner.dispose(Command.FinishType.NORMAL)
            CommandHandle.State.CANCELED -> cmd.inner.dispose(Command.FinishType.CANCELED)
            else -> println("ERR: CommandHandler: Command task $cmd finished with exceptional state ${cmd.state}")
        }
    }

    class CommandHandle(val inner: Command, var onComplete: (suspend () -> Unit)? = null) {

        var state = State.READY

        val handle = launch(context = COMMAND_CTX) {
            val delayTime = 1000 / inner.updateFrequency

            inner.initialize()
            state = State.LOOPING

            while (isActive) {
                inner.execute()

                if (inner.isFinished) {
                    break
                }

                delay(delayTime)
            }
        }

        init {
            handle.invokeOnCompletion {
                runBlocking {
                    state = if (it != null || !inner.isFinished) {
                        State.CANCELED
                    } else {
                        State.FINISHED
                    }

                    commandActor.send(Message.Stop(this@CommandHandle))

                    onComplete?.invoke()
                }
            }
        }

        fun invokeOnCompletion(block: suspend () -> Unit) {
            onComplete = block
        }

        enum class State {
            READY,
            LOOPING,
            FINISHED,
            CANCELED
        }
    }
}

suspend fun Command.start(): Deferred<CommandHandler.CommandHandle> {
    val future = CompletableDeferred<CommandHandler.CommandHandle>()
    CommandHandler.commandActor.send(CommandHandler.Message.Start(this, future))
    return future
}

suspend fun CommandHandler.CommandHandle.stop() {
    CommandHandler.commandActor.send(CommandHandler.Message.Stop(this))
}

suspend fun Command.stop() {
    val handle = CommandHandler.runningCommands
            .values.find { it.inner == this } ?: return
    handle.stop()
}
