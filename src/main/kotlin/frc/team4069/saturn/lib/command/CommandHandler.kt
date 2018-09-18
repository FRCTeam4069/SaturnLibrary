package frc.team4069.saturn.lib.command

import kotlinx.coroutines.experimental.newFixedThreadPoolContext

object CommandHandler {
    private val COMMAND_CTX = newFixedThreadPoolContext(2, "Command Pool")


}