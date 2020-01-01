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

package frc.team4069.saturn.lib.shuffleboard.logging

import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget
import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget

class ShuffleboardComplexWidgetBuilder(private val widget: ComplexWidget) {
    fun position(column: Int, row: Int) {
        widget.withPosition(column, row)
    }

    fun size(width: Int, height: Int) {
        widget.withSize(width, height)
    }

    fun properties(vararg props: Pair<String, Any>) {
        widget.withProperties(mapOf(*props))
    }

    fun build() = widget
}

class ShuffleboardSuppliedWidgetBuilder<T>(private val widget: SuppliedValueWidget<T>) {
    fun position(column: Int, row: Int) {
        widget.withPosition(column, row)
    }

    fun size(width: Int, height: Int) {
        widget.withSize(width, height)
    }

    fun properties(vararg props: Pair<String, Any>) {
        widget.withProperties(mapOf(*props))
    }

//    fun onChange(onChange: (T) -> Unit) {
//        widget.entry.addListener({ notif ->
//            onChange(notif.value as T)
//        }, EntryListenerFlags.kUpdate)
//    }

    fun build(): SuppliedValueWidget<T> {
        return widget
    }
}