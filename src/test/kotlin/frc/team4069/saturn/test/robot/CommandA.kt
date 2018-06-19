package frc.team4069.saturn.test.robot

import frc.team4069.saturn.lib.command.Command

open class CommandA : Command() {
    var ticker = 0

    init {
        requires(TestDriveBase)
    }

    override fun onCreate() {
        TestDriveBase.drive(0.0, 1.0)
    }

    override fun periodic() {
        ticker++
    }

    override fun onFinish() {
        TestDriveBase.drive(0.0, 0.0)
    }

    override val isFinished get() = ticker == 3
}
