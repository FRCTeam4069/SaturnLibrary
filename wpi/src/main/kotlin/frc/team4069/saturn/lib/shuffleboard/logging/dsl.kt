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

import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.wpilibj.shuffleboard.*
import frc.team4069.saturn.lib.SaturnRobot

fun tab(name: String, block: ShuffleboardTabBuilder.() -> Unit): ShuffleboardTab {
    return ShuffleboardTabBuilder(name).apply(block).build()
}

class ShuffleboardTabBuilder(name: String) {

    private val tab = Shuffleboard.getTab(name)

    internal fun build() = tab

    fun textView(name: String, value: () -> Any, block: ShuffleboardWidgetBuilder<Any>.() -> Unit) {
        ShuffleboardWidgetBuilder(tab.add(name, value()).withWidget(BuiltInWidgets.kTextView), value)
                .apply(block)
                .build()
    }

    fun numberSlider(name: String, value: () -> Number, block: ShuffleboardWidgetBuilder<Number>.() -> Unit) {
        ShuffleboardWidgetBuilder(tab.add(name, value()).withWidget(BuiltInWidgets.kNumberSlider), value)
                .apply(block)
                .build()
    }

    fun numberBar(name: String, value: () -> Number, block: ShuffleboardWidgetBuilder<Number>.() -> Unit) {
        ShuffleboardWidgetBuilder(tab.add(name, value()).withWidget(BuiltInWidgets.kNumberBar), value)
                .apply(block)
                .build()
    }

    fun dial(name: String, value: () -> Number, block: ShuffleboardWidgetBuilder<Number>.() -> Unit) {
        ShuffleboardWidgetBuilder(tab.add(name, value()).withWidget(BuiltInWidgets.kDial), value)
                .apply(block)
                .build()
    }

    fun graph(name: String, value: () -> Any, block: ShuffleboardWidgetBuilder<Any>.() -> Unit) {
        ShuffleboardWidgetBuilder(tab.add(name, value()).withWidget(BuiltInWidgets.kGraph), value)
                .apply(block)
                .build()
    }

    fun booleanBox(name: String, value: () -> Boolean, block: ShuffleboardWidgetBuilder<Boolean>.() -> Unit) {
        ShuffleboardWidgetBuilder(tab.add(name, value()).withWidget(BuiltInWidgets.kBooleanBox), value)
                .apply(block)
                .build()
    }

    fun toggleButton(name: String, value: () -> Boolean, block: ShuffleboardWidgetBuilder<Boolean>.() -> Unit) {
        ShuffleboardWidgetBuilder(tab.add(name, value()).withWidget(BuiltInWidgets.kToggleButton), value)
                .apply(block)
                .build()
    }

    fun toggleSwitch(name: String, value: () -> Boolean, block: ShuffleboardWidgetBuilder<Boolean>.() -> Unit) {
        ShuffleboardWidgetBuilder(tab.add(name, value()).withWidget(BuiltInWidgets.kToggleSwitch), value)
                .apply(block)
                .build()
    }

    fun voltageView(name: String, value: () -> Double, block: ShuffleboardWidgetBuilder<Double>.() -> Unit) {
        ShuffleboardWidgetBuilder(tab.add(name, value()).withWidget(BuiltInWidgets.kVoltageView), value)
                .apply(block)
                .build()
    }
}

class ShuffleboardWidgetBuilder<T>(private val widget: SimpleWidget, private val value: () -> T) {
    fun position(column: Int, row: Int) {
        widget.withPosition(column, row)
    }

    fun size(width: Int, height: Int) {
        widget.withSize(width, height)
    }

    fun properties(vararg props: Pair<String, Any>) {
        widget.withProperties(mapOf(*props))
    }

    fun onChange(onChange: (T) -> Unit) {
        widget.entry.addListener({ notif ->
            onChange(notif.value as T)
        }, EntryListenerFlags.kUpdate)
    }

    fun build(): SimpleWidget {
        SaturnRobot.shuffleboardValues.add(widget.entry to (value as () -> Any))

        return widget
    }
}