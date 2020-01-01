package frc.team4069.saturn.lib.shuffleboard.logging

import edu.wpi.first.wpilibj.shuffleboard.*

fun tab(name: String, block: ShuffleboardTabBuilder.() -> Unit): ShuffleboardTab {
    return ShuffleboardTabBuilder(name).apply(block).build()
}

class ShuffleboardTabBuilder(name: String) : ShuffleboardContainerBuilder<ShuffleboardTab>(Shuffleboard.getTab(name)) {
    fun list(name: String, block: ShuffleboardLayoutBuilder.() -> Unit): ShuffleboardLayout {
        return ShuffleboardLayoutBuilder(container, name, BuiltInLayouts.kList)
                .apply(block)
                .build()
    }

    fun grid(name: String, block: ShuffleboardLayoutBuilder.() -> Unit): ShuffleboardLayout {
        return ShuffleboardLayoutBuilder(container, name, BuiltInLayouts.kGrid)
                .apply(block)
                .build()
    }
}

class ShuffleboardLayoutBuilder(parentTab: ShuffleboardTab, name: String, type: LayoutType) : ShuffleboardContainerBuilder<ShuffleboardLayout>(parentTab.getLayout(name, type)) {
    fun position(column: Int, row: Int) {
        container.withPosition(column, row)
    }

    fun size(width: Int, height: Int) {
        container.withSize(width, height)
    }

    fun properties(vararg props: Pair<String, Any>) {
        container.withProperties(mapOf(*props))
    }

}
