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

    val stateMachine = StateMachine(State.values().toSet(), initialState = State.NONE, anyState = State.ANY)

    override fun startCompetition() = runBlocking {
        LiveWindow.setEnabled(false)

        stateMachine.apply {
            onWhile(State.DISABLED) { HAL.observeUserProgramDisabled() }
            onWhile(State.AUTONOMOUS) { HAL.observeUserProgramAutonomous() }
            onWhile(State.TELEOP) { HAL.observeUserProgramTeleop() }
            onWhile(State.TEST) { HAL.observeUserProgramTest() }

            onEnter(State.TEST) { LiveWindow.setEnabled(true) }
            onExit(State.TEST) { LiveWindow.setEnabled(false) }

            onWhile(State.ANY) {
                LiveWindow.updateValues()
                SmartDashboard.updateValues()
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


    protected suspend operator fun Subsystem.unaryPlus() = SubsystemHandler.addSubsystem(this)
}
