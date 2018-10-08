package frc.team4069.saturn.lib.util

import edu.wpi.first.wpilibj.RobotController

/**
 * Class containing logic to count brownouts and notify user code when necessary
 *
 * The [feed] method should be called frequently while the robot is enabled
 */
class BrownoutWatchdog(val notify: () -> Unit) {
    // How many brownouts have occurred so far
    var voltageFaults = 0

    // Is the controller currently browning out
    var browningOut = false

    // Has user code been notified
    var notified = false

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

        if(voltageFaults >= FAULT_THRESHOLD && !notified) {
            notified = true
            notify()
        }
    }

    companion object {
        const val FAULT_THRESHOLD = 3
    }
}