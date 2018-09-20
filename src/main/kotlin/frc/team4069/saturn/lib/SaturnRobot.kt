package frc.team4069.saturn.lib

import edu.wpi.first.wpilibj.TimedRobot
import frc.team4069.saturn.lib.util.MultiMap
import kotlinx.coroutines.experimental.runBlocking

abstract class SaturnRobot : TimedRobot() {
    enum class State {
        ANY,
        DISABLED,
        AUTONOMOUS,
        TELEOP,
        TEST
    }

    private enum class Edge {
        ENTER,
        WHILE,
    }

    private val stateHandlers = MultiMap<Pair<State, Edge>, suspend () -> Unit>()

    abstract suspend fun initialize()

    fun onEnter(state: State, callback: suspend () -> Unit) {
        stateHandlers.putSingle(state to Edge.ENTER, callback)
    }

    fun whileIn(state: State, callback: suspend () -> Unit) {
        stateHandlers.putSingle(state to Edge.WHILE, callback)
    }

    // Stupid repetitive overrides to glue everything together
    override fun robotInit() {
        runBlocking {
            initialize()
        }
    }

    override fun teleopInit() {
        runBlocking {
            stateHandlers.filterKeys { (state, edge) -> state == State.TELEOP || state == State.ANY && edge == Edge.ENTER }
                    .flatMap { (_, callbacks) -> callbacks }
                    .forEach { it() }
        }
    }

    override fun teleopPeriodic() {
        runBlocking {
            stateHandlers.filterKeys { (state, edge) -> state == State.TELEOP || state == State.ANY && edge == Edge.WHILE }
                    .flatMap { (_, callbacks) -> callbacks }
                    .forEach { it() }
        }
    }

    override fun autonomousInit() {
        runBlocking {
            stateHandlers.filterKeys { (state, edge) -> state == State.AUTONOMOUS || state == State.ANY && edge == Edge.ENTER }
                    .flatMap { (_, callbacks) -> callbacks }
                    .forEach { it() }
        }
    }

    override fun autonomousPeriodic() {
        runBlocking {
            stateHandlers.filterKeys { (state, edge) -> state == State.AUTONOMOUS || state == State.ANY && edge == Edge.WHILE }
                    .flatMap { (_, callbacks) -> callbacks }
                    .forEach { it() }
        }
    }

    override fun testInit() {
        runBlocking {
            stateHandlers.filterKeys { (state, edge) -> state == State.TEST || state == State.ANY && edge == Edge.ENTER }
                    .flatMap { (_, callbacks) -> callbacks }
                    .forEach { it() }
        }
    }

    override fun testPeriodic() {
        runBlocking {
            stateHandlers.filterKeys { (state, edge) -> state == State.TEST || state == State.ANY && edge == Edge.WHILE }
                    .flatMap { (_, callbacks) -> callbacks }
                    .forEach { it() }
        }
    }

    override fun disabledInit() {
        runBlocking {
            stateHandlers.filterKeys { (state, edge) -> state == State.DISABLED || state == State.ANY && edge == Edge.ENTER }
                    .flatMap { (_, callbacks) -> callbacks }
                    .forEach { it() }
        }
    }

    override fun disabledPeriodic() {
        runBlocking {
            stateHandlers.filterKeys { (state, edge) -> state == State.DISABLED || state == State.ANY && edge == Edge.WHILE }
                    .flatMap { (_, callbacks) -> callbacks }
                    .forEach { it() }
        }
    }
}
