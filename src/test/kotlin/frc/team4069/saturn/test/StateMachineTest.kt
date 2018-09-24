package frc.team4069.saturn.test

import frc.team4069.saturn.lib.util.StateMachine
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.amshove.kluent.`should be greater or equal to`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should contain all`
import org.amshove.kluent.`should contain`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given

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
        val fsm = StateMachine(State.A).apply {
            onEnter(State.A) {
                println("Entering A")
                entryCalls[State.A] = entryCalls[State.A]!! + 1
            }
            onLeave(State.A) {
                exitCalls[State.A] = exitCalls[State.A]!! + 1
            }

            onEnter(State.B) {
                entryCalls[State.B] = entryCalls[State.B]!! + 1
            }
            onLeave(State.B) {
                exitCalls[State.B] = exitCalls[State.B]!! + 1
            }
        }

        runBlocking {
            fsm.start()

            entryCalls `should contain`(State.A to 1)
            exitCalls.values.forEach { it `should be` 0 }
            fsm.feed(State.A)
            entryCalls `should contain`(State.A to 1)
            exitCalls.values.forEach { it `should be` 0 }
            fsm.feed(State.A)
            entryCalls `should contain`(State.A to 1)
            exitCalls.values.forEach { it `should be` 0 }
            fsm.feed(State.B)
            exitCalls `should contain`(State.A to 1)
            entryCalls `should contain all`(hashMapOf(State.A to 1, State.B to 1))
            fsm.feed(State.B)
            exitCalls `should contain`(State.A to 1)
            entryCalls `should contain all`(hashMapOf(State.A to 1, State.B to 1))
        }
    }

    given("A state machine where listeners tick while a state is present") {
        var t = 0
        val fsm = StateMachine(State.A).apply {
            onWhile(State.B, freq = 25) {
                t++
            }
        }

        runBlocking {
            fsm.start()
            t `should be` 0
            fsm.feed(State.A)
            t `should be` 0
            fsm.feed(State.B)
            delay(80)
            t `should be greater or equal to` 2
            fsm.feed(State.A)
            val lastT = t
            t `should be` lastT
            delay(240)
            // Fairly crude way to make sure that the coroutine stops executing when the SM is fed
            // a value not equal to its desired state
            t `should be` lastT
        }
    }
})
