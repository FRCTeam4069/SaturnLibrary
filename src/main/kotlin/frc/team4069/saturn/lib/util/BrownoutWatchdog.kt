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