package frc.team4069.saturn.test

import frc.team4069.saturn.lib.util.MultiMap
import org.amshove.kluent.`should have value`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object MultiMapTest : Spek({
    given("A multimap") {
        val mm = MultiMap<String, String>()

        on("Single entry added") {
            mm.putSingle("key", "value")

            it("Should have a single valued set") {
                mm `should have value` setOf("value")
            }
        }

        on("Second entry added of the same key") {
            mm.putSingle("key", "value2")
            it("Should append to the existing set, keeping the first value") {
                mm `should have value` setOf("value", "value2")
            }
        }
    }
})