package frc.team4069.saturn.test

import frc.team4069.saturn.lib.util.StateMachine
import kotlinx.coroutines.experimental.runBlocking
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

enum class State {
    A,
    B,
}

enum class StateWithAny {
    A,
    B,
    ANY
}

typealias Call = Pair<State, String>

class StateMachineTest : Spek({
    val entryCalls = hashMapOf(*State.values().map { it to 0 }.toTypedArray())
    val exitCalls = hashMapOf(*State.values().map { it to 0 }.toTypedArray())

    given("A state machine containing entry and exit transition listeners") {
        val stateMachine = StateMachine(initialState = State.A).apply {
            onEnter(State.A) {
                entryCalls[State.A] = entryCalls[State.A]!! + 1
            }
            onExit(State.A) {
                exitCalls[State.A] = exitCalls[State.A]!! + 1
            }

            onEnter(State.B) {
                entryCalls[State.B] = entryCalls[State.B]!! + 1
            }
            onExit(State.B) {
                exitCalls[State.B] = exitCalls[State.B]!! + 1
            }
        }

        it("Should activate entry and exit listeners when satisfied") {
            runBlocking {
                stateMachine.start()
                // Initial entry will cause increment
                entryCalls `should contain` (State.A to 1)
                stateMachine.update(State.A)
                // Same state, same value
                entryCalls `should contain` (State.A to 1)
                stateMachine.update(State.B)
                entryCalls `should contain` (State.B to 1)
                exitCalls `should contain` (State.A to 1)
            }
        }
    }

    given("A state machine with transition listeners between states") {

        var aToBTransCalls = 0

        val stateMachine = StateMachine(initialState = State.A).apply {
            onTransition(State.A, State.B) {
                aToBTransCalls++
            }
        }

        it("Should call the listener when transitioning from A --> B") {
            runBlocking {
                stateMachine.start()
                stateMachine.update(State.B)

                aToBTransCalls `should be equal to` 1
            }
        }
    }

    given("A state machine with an any state") {
        var anyToBTransCalls = 0

        val stateMachine = StateMachine(initialState = StateWithAny.A, anyState = StateWithAny.ANY).apply {
            onTransition(StateWithAny.ANY, StateWithAny.B) {
                anyToBTransCalls++
            }
        }

        it("Should call the listener when any state transitions to state B") {
            runBlocking {
                stateMachine.start()
                stateMachine.update(StateWithAny.B)
                anyToBTransCalls `should be equal to` 1
                stateMachine.update(StateWithAny.B)
                anyToBTransCalls `should be equal to` 1
            }
        }
    }
})