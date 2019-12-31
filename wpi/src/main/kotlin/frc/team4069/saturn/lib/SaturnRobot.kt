/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package frc.team4069.saturn.lib

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.FRCNetComm.tInstances.kLanguage_Kotlin
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.team4069.saturn.lib.commands.SaturnSubsystem
import frc.team4069.saturn.lib.commands.SubsystemHandler
import frc.team4069.saturn.lib.mathematics.units.SIUnit
import frc.team4069.saturn.lib.mathematics.units.Second
import frc.team4069.saturn.lib.mathematics.units.milli

abstract class SaturnRobot(val period: SIUnit<Second> = 20.milli.second) {

    protected val wrappedValue = WpiTimedRobot()

    protected inner class WpiTimedRobot : TimedRobot(period.value) {
        init {
            HAL.report(FRCNetComm.tResourceType.kResourceType_Language, kLanguage_Kotlin)
        }

        override fun robotInit() {
            this@SaturnRobot.robotInit()
            LiveWindow.disableAllTelemetry()
            SubsystemHandler.lateInit()
        }

        override fun autonomousInit() {
            this@SaturnRobot.autonomousInit()
            SubsystemHandler.autoReset()
        }

        override fun teleopInit() {
            this@SaturnRobot.teleopInit()
            SubsystemHandler.teleopReset()
        }

        override fun testInit() {
            this@SaturnRobot.testInit()
        }

        override fun disabledInit() {
            this@SaturnRobot.disabledInit()
            SubsystemHandler.setNeutral()
        }

        override fun robotPeriodic() {
            this@SaturnRobot.robotPeriodic()
            CommandScheduler.getInstance().run()
        }

        override fun autonomousPeriodic() {
            this@SaturnRobot.autonomousPeriodic()
        }

        override fun teleopPeriodic() {
            this@SaturnRobot.teleopPeriodic()
        }

        override fun testPeriodic() {
            this@SaturnRobot.testPeriodic()
        }

        override fun disabledPeriodic() {
            this@SaturnRobot.disabledPeriodic()
        }
    }

    protected open fun robotInit() {}
    protected open fun autonomousInit() {}
    protected open fun teleopInit() {}
    protected open fun testInit() {}
    protected open fun disabledInit() {}

    protected open fun robotPeriodic() {}
    protected open fun autonomousPeriodic() {}
    protected open fun teleopPeriodic() {}
    protected open fun testPeriodic() {}
    protected open fun disabledPeriodic() {}

    protected fun addToSubsystemHandler(subsystem: SaturnSubsystem) {
        SubsystemHandler.add(subsystem)
    }

    open operator fun SaturnSubsystem.unaryPlus() {
        addToSubsystemHandler(this)
    }

    fun start() {
        RobotBase.startRobot { wrappedValue }
    }
}
