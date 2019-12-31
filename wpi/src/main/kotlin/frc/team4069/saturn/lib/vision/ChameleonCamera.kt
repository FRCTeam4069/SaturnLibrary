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

package frc.team4069.saturn.lib.vision

import frc.team4069.saturn.lib.mathematics.units.degree
import frc.team4069.saturn.lib.nt.SaturnNetworkTable
import frc.team4069.saturn.lib.nt.delegate
import frc.team4069.saturn.lib.nt.get

/**
 * A camera being operated by a coprocessor running Chameleon Vision.
 *
 * The values of the pipeline are sent over NetworkTables by the coprocessor, and include the distance
 * and angle of the target. Some values can be modified by user code which will alter the behaviour of the coprocessor.
 */
//TODO: Some of these things could be improved with units, but i dont know the units
class ChameleonCamera(cameraName: String) {
    private val cameraTable = kRootTable.getSubTable(cameraName)

    /**
     * The timestamp corresponding to when the coprocessor received the frame that led to these results.
     *
     * This can be compared against the timestamp of the FPGA to compensate for latency from networking and pipeline delay.
     */
    val timestamp by cameraTable["timestamp"].delegate(0.0)

    /**
     * The pipeline that is currently being used.
     *
     * Changing this value will alter the pipeline the coprocessor will apply to camera data.
     */
    var pipeline by cameraTable["pipeline"].delegate(0.0)

    /**
     */
    val distance by cameraTable["distance"].delegate(0.0)

    /**
     * Set to false if the pipeline encountered errors when processing
     * (e.g. no targets in view of the camera)
     */
    val isValid by cameraTable["is_valid"].delegate(false)

    /**
     * The vertical angle of the target
     */
    private val _pitch by cameraTable["pitch"].delegate(0.0)
    val pitch
        get() = _pitch.degree

    /**
     * Whether the coprocessor should enable driver mode to the camera feed.
     *
     * Driver mode acts by disabling the image pipeline, including processing and drawing,
     * and by changing the brightness and exposure to make targets easier to see by the driver.
     */
    var driverMode by cameraTable["driver_mode"].delegate(false)

    /**
     * The horizontal angle of the target.
     */
    private val _yaw by cameraTable["yaw"].delegate(0.0)
    val yaw get() = _yaw.degree

    companion object {
        val kRootTable = SaturnNetworkTable.getTable("chameleon-vision")
    }
}