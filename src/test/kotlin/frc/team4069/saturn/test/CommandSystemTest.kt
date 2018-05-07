package frc.team4069.saturn.test

import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.CommandGroup
import frc.team4069.saturn.lib.command.Scheduler
import frc.team4069.saturn.test.robot.CommandA
import frc.team4069.saturn.test.robot.CommandB
import frc.team4069.saturn.test.robot.TestDriveBase
import frc.team4069.saturn.test.robot.TestSubsystem
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object CommandSystemTest : Spek({
    describe("A command scheduler") {
        val scheduler = Scheduler

        it("Should run a drive straight command for 3 ticks") {
            val command = CommandA()

            scheduler.add(command)
            repeat(2) {
                scheduler.run()
                scheduler.queuedCommands shouldContain command
                checkSpeeds(1.0)
            }
            scheduler.run()
            assert(scheduler.queuedCommands.isEmpty())
            scheduler.queuedCommands.shouldBeEmpty()
            checkSpeeds(0.0)
            Scheduler.clear()
        }

        it("Should suspend a command when a newer command is added") {
            val drive = TestDriveBase

            val command = object : CommandA() {
                override fun onSuspend() {
                    TestDriveBase.drive(0.0, 0.0)
                }

                override fun onResume() {
                    TestDriveBase.drive(0.0, 1.0)
                }
            }

            val commandB = CommandB()


            scheduler.add(command)

            repeat(2) {
                scheduler.run()
                assert(scheduler.queuedCommands.contains(command))
                checkSpeeds(1.0)
            }
            scheduler.add(commandB)
            scheduler.queuedCommands shouldContain commandB
            scheduler.suspendedCommands shouldContain command
            checkSpeeds(0.5)
            scheduler.run()
            scheduler.queuedCommands shouldContain command
            scheduler.suspendedCommands.shouldBeEmpty()
            checkSpeeds(1.0)
            scheduler.run()
            scheduler.queuedCommands.shouldBeEmpty()
            checkSpeeds(0.0)
            Scheduler.clear()
        }

        it("Should schedule all parallel commands in a command group at once") {
            val commandA = CommandA()
            val commandB = object : Command() {
                override fun periodic() {
                    TestSubsystem.update()
                }

                override val isFinished = false
            }

            val group = object : CommandGroup() {
                init {
                    addParallel(commandA)
                    addParallel(commandB)
                }
            }

            scheduler.add(group)

            scheduler.queuedCommands shouldContainAll arrayOf(commandA, commandB)
            Scheduler.clear()
        }

        it("Should schedule a command and run it when a button scheduler is fired") {
            var buttonPressed = false
            val command = CommandA()
            val scheduler = object : Runnable {
                var pressed = false

                override fun run() {
                    if (buttonPressed && !pressed) {
                        pressed = true
                        Scheduler.add(command)
                    } else if (!buttonPressed && pressed) {
                        Scheduler.cancel(command)
                    }
                }
            }

            Scheduler.addButtonScheduler(scheduler)

            Scheduler.queuedCommands.shouldBeEmpty()
            Scheduler.run()
            Scheduler.queuedCommands.shouldBeEmpty()
            buttonPressed = true
            Scheduler.run()
            Scheduler.queuedCommands shouldContain command
            buttonPressed = false
            Scheduler.run()
            Scheduler.queuedCommands.shouldBeEmpty()
            Scheduler.clear()
        }
    }
})

fun checkSpeeds(speed: Double) {
    TestDriveBase.leftDrive.speed shouldEqual speed
    TestDriveBase.rightDrive.speed shouldEqual speed
}
