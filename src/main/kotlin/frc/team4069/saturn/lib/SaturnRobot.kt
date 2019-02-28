package frc.team4069.saturn.lib

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.saturn.lib.commands.SaturnSubsystem
import frc.team4069.saturn.lib.commands.SubsystemHandler
import frc.team4069.saturn.lib.hid.SaturnHID
import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.derivedunits.hertz
import frc.team4069.saturn.lib.mathematics.units.millisecond
import frc.team4069.saturn.lib.util.BrownoutWatchdog
import frc.team4069.saturn.lib.util.launchFrequency
import kotlinx.coroutines.GlobalScope

const val kLanguageKotlin = 6

abstract class SaturnRobot(val period: Time = 20.millisecond) : RobotBase() {
    companion object {
        @Suppress("LateinitUsage")
        lateinit var INSTANCE: SaturnRobot
            private set
    }

    init {
        @Suppress("LeakingThis")
        INSTANCE = this
    }

    enum class Mode {
        NONE,
        DISABLED,
        AUTONOMOUS,
        TELEOP,
        TEST
    }

    private val brownoutWatchdog = BrownoutWatchdog(::notifyBrownout)
    var currentMode = Mode.NONE
        private set
    private val controls = mutableListOf<SaturnHID<*>>()

    var initialized = false
        private set

    protected abstract fun initialize()

    protected open suspend fun periodic() {}

    protected open fun notifyBrownout() {}

    override fun startCompetition() {
        HAL.report(FRCNetComm.tResourceType.kResourceType_Language, kLanguageKotlin)
        LiveWindow.setEnabled(false)

        initialize()
        SubsystemHandler.lateInit()
        initialized = true

        println("[Robot] Initialized")

        HAL.observeUserProgramStarting()

        GlobalScope.launchFrequency((1 / period.second).hertz) {
//            m_ds.waitForData()

            val newMode = when {
                isDisabled -> Mode.DISABLED
                isAutonomous -> Mode.AUTONOMOUS
                isOperatorControl -> Mode.TELEOP
                isTest -> Mode.TEST
                else -> TODO("Robot is in invalid mode!")
            }

            if(currentMode != newMode) {
                when(newMode) {
                    Mode.DISABLED -> {
                        disabledInit()
                        SubsystemHandler.zeroOutputs()
                    }
                    Mode.AUTONOMOUS -> {
                        autonomousInit()
                        SubsystemHandler.autoReset()
                    }
                    Mode.TELEOP -> {
                        teleopInit()
                        SubsystemHandler.teleopReset()
                    }
                    else -> {}
                }
            }

            currentMode = newMode

            when(newMode) {
                Mode.DISABLED -> HAL.observeUserProgramDisabled()
                Mode.AUTONOMOUS -> HAL.observeUserProgramAutonomous()
                Mode.TELEOP -> HAL.observeUserProgramTeleop()
                Mode.TEST -> HAL.observeUserProgramTest()
                Mode.NONE -> throw IllegalStateException("Mode cannot be NONE")
            }

            brownoutWatchdog.feed()
            controls.forEach { it.update() }
            SmartDashboard.updateValues()
            Shuffleboard.update()

            Scheduler.getInstance().run()

            periodic()
        }

        while(true) {
            Thread.sleep(60 * 60)
        }
    }

    protected open fun autonomousInit() {}
    protected open fun teleopInit() {}
    protected open fun disabledInit() {}

    protected operator fun SaturnSubsystem.unaryPlus() = SubsystemHandler.addSubsystem(this)
    protected operator fun SaturnHID<*>.unaryPlus() = controls.add(this)
}
