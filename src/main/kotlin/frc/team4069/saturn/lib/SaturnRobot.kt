package frc.team4069.saturn.lib

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

    val stateMachine = StateMachine(initialState = State.NONE, anyState = State.ANY)

    override fun startCompetition() = runBlocking {
        LiveWindow.setEnabled(false)

        stateMachine.apply {
            onWhile(State.DISABLED) { HAL.observeUserProgramDisabled() }
            onWhile(State.AUTONOMOUS) {
                HAL.observeUserProgramAutonomous()
                autonomousPeriodic()
            }
            onWhile(State.TELEOP) {
                HAL.observeUserProgramTeleop()
                teleoperatedPeriodic()
            }
            onWhile(State.TEST) {
                HAL.observeUserProgramTest()
                testPeriodic()
            }

            onEnter(State.TEST) {
                LiveWindow.setEnabled(true)
                testInit()
            }
            onExit(State.TEST) { LiveWindow.setEnabled(false) }

            onEnter(State.AUTONOMOUS) {
                autonomousInit()
            }

            onEnter(State.TELEOP) {
                teleoperatedInit()
            }

            onWhile(State.ANY) {
                LiveWindow.updateValues()
                SmartDashboard.updateValues()
            }

            onTransition(State.TELEOP, State.DISABLED) {
                disabled()
            }
        }


        initialize()
        SubsystemHandler.startDefaultCommands()

        stateMachine.start()

        HAL.observeUserProgramStarting()

        while(isActive) {
            m_ds.waitForData()

            val newState = when {
                isDisabled -> State.DISABLED
                isAutonomous -> State.AUTONOMOUS
                isOperatorControl -> State.TELEOP
                isTest -> State.TEST
                else -> throw IllegalStateException("Robot is in invalid state")
            }

            stateMachine.update(newState)

        }
    }


    abstract suspend fun initialize()

    open suspend fun autonomousInit() {}
    open suspend fun teleoperatedInit() {}
    open suspend fun testInit() {}

    open suspend fun autonomousPeriodic() {}
    open suspend fun teleoperatedPeriodic() {}
    open suspend fun testPeriodic() {}

    open suspend fun disabled() {}


    protected suspend operator fun SubsystemHandler.plusAssign(subsystem: Subsystem) = SubsystemHandler.addSubsystem(subsystem)
}
