package frc.team4069.saturn.test

import frc.team4069.saturn.lib.util.StateMachine
import kotlinx.coroutines.experimental.runBlocking
import org.amshove.kluent.`should contain`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given

enum class State {
    A,
    B,
}

typealias Call = Pair<State, String>

class StateMachineTest : Spek({
    val entryCalls = hashMapOf(*State.values().map { it to 0 }.toTypedArray())
    val exitCalls = hashMapOf(*State.values().map { it to 0 }.toTypedArray())

    given("A state machine") {
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
})