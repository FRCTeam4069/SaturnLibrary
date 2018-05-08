package frc.team4069.saturn.lib.command

class DelayCommand(private val timeout: Long) : Command() {
    private var target = -1L

    constructor(seconds: Int) : this(seconds * 1000L)

    override fun onCreate() {
        target = System.currentTimeMillis() + timeout
    }

    override val isFinished: Boolean
        get() = System.currentTimeMillis() >= target && target != -1L
}