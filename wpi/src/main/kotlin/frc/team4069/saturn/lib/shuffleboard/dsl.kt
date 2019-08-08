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

package frc.team4069.saturn.lib.shuffleboard

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

fun <T> chooser(name: String, block: SendableChooserDsl<T>.() -> Unit): SendableChooser<T> {
    val chooser = SendableChooserDsl(SendableChooser<T>()).apply(block).finalize()
    SmartDashboard.putData(name, chooser)
    return chooser
}

class SendableChooserDsl<T>(val chooser: SendableChooser<T>) {
    private val values = mutableMapOf<String, T>()
    private var defaultKey: String? = null

    operator fun String.plusAssign(value: T) {
        values[this] = value
    }

    operator fun String.unaryPlus() {
        defaultKey = this
    }

    fun finalize(): SendableChooser<T> {
        if (defaultKey != null) {
            chooser.setDefaultOption(defaultKey!!, values.remove(defaultKey!!)!!)
        }

        values.forEach { (s, v) -> chooser.addOption(s, v) }
        return chooser
    }
}
