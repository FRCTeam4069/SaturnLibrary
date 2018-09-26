package frc.team4069.saturn.lib

import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.hal.HAL
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.saturn.lib.command.Subsystem
import frc.team4069.saturn.lib.command.SubsystemHandler
import frc.team4069.saturn.lib.hid.SaturnHID
import frc.team4069.saturn.lib.util.StateMachine
import kotlinx.coroutines.experimental.runBlocking

/**
 * Base robot class for programs utilizing SaturnShell
 *
 * Mimics the layout of wpilib Iterative and Timed robot bases, but with added support for coroutines and custom comamnds.
 */
abstract class SaturnRobot : RobotBase() {
    enum class State {
        NONE,
        ANY,
        DISABLED,
        AUTONOMOUS,
        TELEOP,
        TEST
    }

    /**
     * Internal state machine used to run code blocks based on the current mode the robot is in
     *
     * Starts in [State.NONE], updated in [startCompetition] with the state of the robot according to the driver station
     */
    private val stateMachine = StateMachine(State.NONE, anyState = State.ANY)

    /**
     * Function called when robot user code is started, contains initialization code
     */
    override fun startCompetition() = runBlocking {
        LiveWindow.setEnabled(false)

        // Instructs the state machine to perform certain actions to link the iterative-like APIs to the behaviour of the robot
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

        // Initialize custom user code, notify the HAL, and begin looping to update state
        robotInit()

        SubsystemHandler.startDefaultCommands()
        HAL.observeUserProgramStarting()

        stateMachine.start()

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


    /**
     * Mandatory function called to initialize custom user code
     *
     * Subsystem, controller, and other registration activities should be put here
     */
    abstract suspend fun robotInit()

    /**
     * Function called once when the robot state transitions into autonomous mode
     */
    open suspend fun autonomousInit() {}

    /**
     * Function called once when the robot state transitions into teleoperated mode
     */
    open suspend fun teleopInit() {}

    /**
     * Function called once when the robot state transitions into disabled mode
     */
    open suspend fun disabledInit() {}

    /**
     * Function called once when the robot state transitions into test mode
     */
    open suspend fun testInit() {}

    /**
     * Function called frequently while the robot is in autonomous mode
     */
    open suspend fun autonomousPeriodic() {}

    /**
     * Function called frequently while the robot is in teleoperated mode
     */
    open suspend fun teleopPeriodic() {}

    /**
     * Function called frequently while the robot is in disabled mode
     */
    open suspend fun disabledPeriodic() {}

    /**
     * Function called frequently while the robot is in test mode
     */
    open suspend fun testPeriodic() {}


    /**
     * Function/operator to register a Subsystem. Must be called during [robotInit], calling this function during any other mode is invalid
     */
    protected suspend operator fun Subsystem.unaryPlus() = SubsystemHandler.addSubsystem(this)

    /**
     * Function/operator to register a controller to check for updates during teleop. Must be called during [robotInit]
     */
    protected suspend operator fun SaturnHID<*>.unaryPlus() = stateMachine.onWhile(State.TELEOP) { update() }
}
