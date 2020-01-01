package frc.team4069.saturn.lib.shuffleboard.logging

import edu.wpi.cscore.VideoSource
import edu.wpi.first.wpilibj.*
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import edu.wpi.first.wpilibj.interfaces.Accelerometer
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser

abstract class ShuffleboardContainerBuilder<T: ShuffleboardContainer>(val container: T) {
    fun textView(name: String, value: () -> Any, block: ShuffleboardSuppliedWidgetBuilder<String>.() -> Unit) {
        ShuffleboardSuppliedWidgetBuilder(container.addString(name) { value().toString() }.withWidget(BuiltInWidgets.kTextView))
                .apply(block)
                .build()
    }

    fun numberSlider(name: String, value: () -> Number, block: ShuffleboardSuppliedWidgetBuilder<Double>.() -> Unit) {
        ShuffleboardSuppliedWidgetBuilder(container.addNumber(name) { value().toDouble() }.withWidget(BuiltInWidgets.kNumberSlider))
                .apply(block)
                .build()
    }

    fun numberBar(name: String, value: () -> Number, block: ShuffleboardSuppliedWidgetBuilder<Double>.() -> Unit) {
        ShuffleboardSuppliedWidgetBuilder(container.addNumber(name) { value().toDouble() }.withWidget(BuiltInWidgets.kNumberBar))
                .apply(block)
                .build()
    }

    fun dial(name: String, value: () -> Number, block: ShuffleboardSuppliedWidgetBuilder<Double>.() -> Unit) {
        ShuffleboardSuppliedWidgetBuilder(container.addNumber(name) { value().toDouble() }.withWidget(BuiltInWidgets.kDial))
                .apply(block)
                .build()
    }

    fun booleanBox(name: String, value: () -> Boolean, block: ShuffleboardSuppliedWidgetBuilder<Boolean>.() -> Unit) {
        ShuffleboardSuppliedWidgetBuilder(container.addBoolean(name, value).withWidget(BuiltInWidgets.kBooleanBox))
                .apply(block)
                .build()
    }

    fun toggleButton(name: String, value: () -> Boolean, block: ShuffleboardSuppliedWidgetBuilder<Boolean>.() -> Unit) {
        ShuffleboardSuppliedWidgetBuilder(container.addBoolean(name, value).withWidget(BuiltInWidgets.kToggleButton))
                .apply(block)
                .build()
    }

    fun toggleSwitch(name: String, value: () -> Boolean, block: ShuffleboardSuppliedWidgetBuilder<Boolean>.() -> Unit) {
        ShuffleboardSuppliedWidgetBuilder(container.addBoolean(name, value).withWidget(BuiltInWidgets.kToggleSwitch))
                .apply(block)
                .build()
    }

    fun voltageView(name: String, value: () -> Double, block: ShuffleboardSuppliedWidgetBuilder<Double>.() -> Unit) {
        ShuffleboardSuppliedWidgetBuilder(container.addNumber(name, value).withWidget(BuiltInWidgets.kVoltageView))
                .apply(block)
                .build()
    }

    fun pdp(name: String, value: PowerDistributionPanel, block: ShuffleboardComplexWidgetBuilder.() -> Unit) {
        ShuffleboardComplexWidgetBuilder(container.add(name, value).withWidget(BuiltInWidgets.kPowerDistributionPanel))
                .apply(block)
                .build()
    }

    fun <T> comboBox(name: String, value: SendableChooser<T>, block: ShuffleboardComplexWidgetBuilder.() -> Unit) {
        ShuffleboardComplexWidgetBuilder(container.add(name, value).withWidget(BuiltInWidgets.kComboBoxChooser))
                .apply(block)
                .build()
    }

    fun <T> splitButtonChooser(name: String, value: SendableChooser<T>, block: ShuffleboardComplexWidgetBuilder.() -> Unit) {
        ShuffleboardComplexWidgetBuilder(container.add(name, value).withWidget(BuiltInWidgets.kSplitButtonChooser))
                .apply(block)
                .build()
    }

    fun encoder(name: String, value: Encoder, block: ShuffleboardComplexWidgetBuilder.() -> Unit) {
        ShuffleboardComplexWidgetBuilder(container.add(name, value).withWidget(BuiltInWidgets.kEncoder))
                .apply(block)
                .build()
    }

    fun <T> speedController(name: String, value: T, block: ShuffleboardComplexWidgetBuilder.() -> Unit)
        where T : SpeedController, T: Sendable
    {
        ShuffleboardComplexWidgetBuilder(container.add(name, value).withWidget(BuiltInWidgets.kSpeedController))
                .apply(block)
                .build()
    }

    fun accelerometer(name: String, value: AnalogAccelerometer, block: ShuffleboardComplexWidgetBuilder.() -> Unit) {
        ShuffleboardComplexWidgetBuilder(container.add(name, value).withWidget(BuiltInWidgets.kAccelerometer))
                .apply(block)
                .build()
    }

    fun <T> threeAxisAccelerometer(name: String, value: T, block: ShuffleboardComplexWidgetBuilder.() -> Unit)
        where T: Accelerometer, T: Sendable
    {
        if(value !is ADXL345_I2C && value !is ADXL345_SPI && value !is ADXL362) {
            throw IllegalArgumentException("Invalid parameter to 3-axis accelerometer: $value")
        }
        ShuffleboardComplexWidgetBuilder(container.add(name, value).withWidget(BuiltInWidgets.k3AxisAccelerometer))
                .apply(block)
                .build()
    }

    fun gyro(name: String, value: GyroBase, block: ShuffleboardComplexWidgetBuilder.() -> Unit)
    {
        ShuffleboardComplexWidgetBuilder(container.add(name, value).withWidget(BuiltInWidgets.kGyro))
                .apply(block)
                .build()
    }

    fun differentialDrive(name: String, value: DifferentialDrive, block: ShuffleboardComplexWidgetBuilder.() -> Unit) {
        ShuffleboardComplexWidgetBuilder(container.add(name, value).withWidget(BuiltInWidgets.kDifferentialDrive))
                .apply(block)
                .build()
    }

    fun cameraStream(name: String, value: VideoSource, block: ShuffleboardComplexWidgetBuilder.() -> Unit) {
        ShuffleboardComplexWidgetBuilder(container.add(name, value).withWidget(BuiltInWidgets.kCameraStream))
                .apply(block)
                .build()

    }

    internal fun build() = container
}
