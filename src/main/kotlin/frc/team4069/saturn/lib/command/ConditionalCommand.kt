package frc.team4069.saturn.lib.command

abstract class ConditionalCommand(private val onTrue: Command? = null, private val onFalse: Command? = null) : Command() {
    abstract val condition: Boolean

    var chosenCommand: Command? = null

    override fun onCreate() {
        chosenCommand = if(condition) {
            onTrue
        }else {
            onFalse
        }

        chosenCommand?.onCreate()
    }

    override fun onCancelled() {
        chosenCommand?.onCancelled()
    }

    override fun onFinish() {
        chosenCommand?.onFinish()
    }

    override fun onResume() {
        chosenCommand?.onResume()
    }

    override fun onSuspend() {
        chosenCommand?.onSuspend()
    }

    override val isFinished
        get() = chosenCommand?.isFinished ?: true
}