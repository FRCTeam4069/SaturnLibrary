package frc.team4069.saturn.test

import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.Scheduler
import frc.team4069.saturn.test.robot.TestDriveBase
import org.amshove.kluent.`should contain all`
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object CommandSystemTest : Spek({
    describe("A command scheduler") {
        val scheduler = Scheduler

        it("Should run a drive straight command for 3 ticks") {
            val drive = TestDriveBase
            val command = object : Command() {
                var ticker = 0

                override fun initialize() {
                    drive.drive(0.0, 1.0)
                }

                override fun periodic() {
                    ticker++
                }

                override fun finished() {
                    drive.drive(0.0, 0.0)
                }

                override val isFinished get() = ticker == 3
            }

            scheduler.add(command)
            repeat(2) {
                scheduler.run()
                scheduler.queuedCommands shouldContain command
                checkSpeeds(drive, 1.0)
            }
            scheduler.run()
            assert(scheduler.queuedCommands.isEmpty())
            scheduler.queuedCommands `should contain all` emptyArray()
            checkSpeeds(drive, 0.0)
        }

        it("Should suspend a command when a newer command is added") {
            val drive = TestDriveBase

            val command = object : Command() {
                var ticker = 0

                override fun initialize() {
                    requires(drive)
                    drive.drive(0.0, 1.0)
                }

                override fun periodic() {
                    ticker++
                }

                override fun suspended() {
                    drive.drive(0.0, 0.0)
                }

                override fun resumed() {
                    drive.drive(0.0, 1.0)
                }

                override fun finished() {
                    drive.drive(0.0, 0.0)
                }

                override val isFinished: Boolean
                    get() = ticker == 3

                override fun toString() = "CommandA(ticker=$ticker)"
            }

            val commandB = object : Command() {
                var ticker = 0

                init {
                    requires(drive)
                }

                override fun initialize() {
                    drive.drive(0.0, 0.5)
                }

                override fun periodic() {
                    ticker++
                }

                override val isFinished: Boolean
                    get() = ticker == 1
            }

            scheduler.add(command)

            repeat(2) {
                scheduler.run()
                assert(scheduler.queuedCommands.contains(command))
                checkSpeeds(drive, 1.0)
            }
            scheduler.add(commandB)
            scheduler.queuedCommands shouldContain commandB
            scheduler.suspendedCommands shouldContain command
            checkSpeeds(drive, 0.5)
            scheduler.run()
            scheduler.queuedCommands shouldContain command
            scheduler.suspendedCommands `should contain all` emptyArray()
            checkSpeeds(drive, 1.0)
            scheduler.run()
            scheduler.queuedCommands `should contain all` emptyArray()
            checkSpeeds(drive, 0.0)
        }
    }
})

fun checkSpeeds(drive: TestDriveBase, speed: Double) {
    drive.leftDrive.speed shouldEqual speed
    drive.rightDrive.speed shouldEqual speed
}
