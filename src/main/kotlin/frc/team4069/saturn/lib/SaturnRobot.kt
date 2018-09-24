package frc.team4069.saturn.lib

import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.hal.HAL
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.saturn.lib.command.Subsystem
import frc.team4069.saturn.lib.command.SubsystemHandler
import frc.team4069.saturn.lib.util.StateMachine
import kotlinx.coroutines.experimental.runBlocking

abstract class SaturnRobot : RobotBase() {
    enum class State {
        NONE,
        ANY,
        DISABLED,
        AUTONOMOUS,
        TELEOP,
        TEST
    }

    private val stateMachine = StateMachine(State.NONE, anyState = State.ANY)

    private val looper = Notifier {
        runBlocking {

        }
    }

    override fun startCompetition() = runBlocking {
        LiveWindow.setEnabled(false)

        stateMachine.apply {
            onEnter(State.DISABLED) { disabledInit() }
            onWhile(State.DISABLED) {
                HAL.observeUserProgramDisabled()
                disabledPeriodic()
            }

            onEnter(State.AUTONOMOUS) { autonomousInit() }
            onWhile(State.AUTONOMOUS) {
                HAL.observeUserProgramAutonomous()
                autonomousPeriodic()
            }

            onEnter(State.TELEOP) { teleopInit() }
            onWhile(State.TELEOP) {
                HAL.observeUserProgramTeleop()
                teleopPeriodic()
            }

            onEnter(State.TEST) {
                testInit()
                LiveWindow.setEnabled(true)
            }
            onWhile(State.TEST) {
                testPeriodic()
                LiveWindow.updateValues()
            }
            onLeave(State.TEST) {
                LiveWindow.setEnabled(false)
            }

            onWhile(State.ANY) {
                SmartDashboard.updateValues()
            }
        }

        robotInit()

        SubsystemHandler.startDefaultCommands()
        HAL.observeUserProgramStarting()

        stateMachine.start()

        looper.startPeriodic(0.02)

        while (isActive) {
            m_ds.waitForData()

            val newState = when {
                isDisabled -> State.DISABLED
                isAutonomous -> State.AUTONOMOUS
                isOperatorControl -> State.TELEOP
                isTest -> State.TEST
                else -> throw IllegalStateException("Robot is in invalid state")
            }

            stateMachine.feed(newState)
        }
    }


    abstract suspend fun robotInit()

    open suspend fun autonomousInit() {}
    open suspend fun teleopInit() {}
    open suspend fun disabledInit() {}
    open suspend fun testInit() {}

    open suspend fun autonomousPeriodic() {}
    open suspend fun teleopPeriodic() {}
    open suspend fun disabledPeriodic() {}
    open suspend fun testPeriodic() {}


    protected suspend operator fun Subsystem.unaryPlus() = SubsystemHandler.addSubsystem(this)
//    protected suspend operator fun SaturnHID<*>.unaryPlus() = stateMachine.onWhile(State.TELEOP) { update() }
}
