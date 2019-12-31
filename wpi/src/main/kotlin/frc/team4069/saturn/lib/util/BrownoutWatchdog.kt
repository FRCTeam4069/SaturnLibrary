/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package frc.team4069.saturn.lib.util

import edu.wpi.first.wpilibj.RobotController

/**
 * Class containing logic to count brownouts and notify user code when necessary
 *
 * The [feed] method should be called frequently while the robot is enabled
 */
class BrownoutWatchdog(val notify: () -> Unit) {
    // How many brownouts have occurred so far
    private var voltageFaults = 0

    // Is the controller currently browning out
    private var browningOut = false

    // Has user code been notified
    private var notified = false

    var threshold = FAULT_THRESHOLD

    /**
     * Updates the internal state of the watchdog, notifying user code if
     * more than [FAULT_THRESHOLD] brownouts have occurred.
     */
    fun feed() {
        if(RobotController.isBrownedOut()) {
            if(!browningOut) {
                browningOut = true
                voltageFaults++
            }
        }else {
            if(browningOut) {
                browningOut = false
            }
        }

        if(voltageFaults >= threshold && !notified) {
            notified = true
            notify()
        }
    }

    companion object {
        const val FAULT_THRESHOLD = 10
    }
}