import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget", title = "Kotlin Wasm Html Interop") {
        CompositionLocalProvider(LocalLayerContainer provides document.getElementById("container")!!) {
            App()
        }
    }
}