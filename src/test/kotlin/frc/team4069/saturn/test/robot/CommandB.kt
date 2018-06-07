package frc.team4069.saturn.test.robot

import frc.team4069.saturn.lib.command.Command

class CommandB : Command() {
    var ticker = 0

    init {
        requires(TestDriveBase)
    }

    override fun onCreate() {
        TestDriveBase.drive(0.0, 0.5)
    }

    override fun periodic() {
        ticker++
    }

    override val isFinished: Boolean
        get() = ticker == 1
}
