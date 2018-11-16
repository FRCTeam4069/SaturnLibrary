package frc.team4069.path

import tornadofx.App
import tornadofx.View
import tornadofx.label
import tornadofx.vbox

class SplineUI : App(SplineView::class)

class SplineView : View() {
    override val root = vbox {
        label("Hello, World!")
    }

}
