package frc.team4069.saturn.lib.state

import kotlinx.coroutines.experimental.DisposableHandle

interface ObservableHandle : DisposableHandle {
    operator fun plus(handle: ObservableHandle): ObservableHandle {
        fun mainDispose() = dispose()
        return object : ObservableHandle {
            override fun dispose() {
                mainDispose()
                handle.dispose()
            }
        }
    }
}

object NonObservableHandle : ObservableHandle {
    override fun dispose() {
    }
}
