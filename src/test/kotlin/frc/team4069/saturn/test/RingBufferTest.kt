package frc.team4069.saturn.test

import frc.team4069.saturn.lib.util.MovingAverage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object RingBufferTest : Spek({
    describe("A ring buffer with capacity 2, elements [3, 4]") {
        val buffer = MovingAverage(2)
        buffer.add(3.0)
        buffer.add(4.0)

        on("Adding another element") {
            buffer.add(5.0)
            it("Should evict 3") {
                assert(buffer.backing.contentEquals(doubleArrayOf(4.0, 5.0).toTypedArray()))
            }
        }
    }
})

