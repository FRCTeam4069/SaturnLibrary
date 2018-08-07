package frc.team4069.saturn.lib.command

import java.util.concurrent.atomic.AtomicLong

abstract class Subsystem(val name: String) {
    companion object {
        val ID = AtomicLong()
    }

    constructor() : this("Subsystem ${ID.incrementAndGet()}")

    open var defaultCommand: Command? = null
}