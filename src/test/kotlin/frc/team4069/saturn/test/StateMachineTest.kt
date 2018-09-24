package frc.team4069.saturn.test

import frc.team4069.saturn.lib.util.StateMachine
import kotlinx.coroutines.experimental.runBlocking
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

            fsm.feed(State.A)
            println("Entry $entryCalls")
            println("Exit $exitCalls")
            fsm.feed(State.A)
            println("Entry $entryCalls")
            println("Exit $exitCalls")
            fsm.feed(State.B)
            println("Entry $entryCalls")
            println("Exit $exitCalls")
            fsm.feed(State.B)
            println("Entry $entryCalls")
            println("Exit $exitCalls")
        }
    }
})
