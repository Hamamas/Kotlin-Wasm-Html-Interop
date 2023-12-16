import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateObserver
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.round
import kotlinx.browser.document
import kotlinx.dom.appendElement
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import kotlin.random.Random

val NoOpUpdate: Element.() -> Unit = {}

val randomColor : Color
    get() = Color(Random.nextLong(0xFFFFFFFF))

private class ComponentInfo<T : Element> {
    lateinit var container: Element
    lateinit var component: T
    lateinit var updater: Updater<T>
}


private class FocusSwitcher<T : Element>(
    private val info: ComponentInfo<T>,
    private val focusManager: FocusManager
) {
    private val backwardRequester = FocusRequester()
    private val forwardRequester = FocusRequester()
    private var isRequesting = false

    fun moveBackward() {
        try {
            isRequesting = true
            backwardRequester.requestFocus()
        } finally {
            isRequesting = false
        }
        focusManager.moveFocus(FocusDirection.Previous)
    }

    fun moveForward() {
        try {
            isRequesting = true
            forwardRequester.requestFocus()
        } finally {
            isRequesting = false
        }
        focusManager.moveFocus(FocusDirection.Next)
    }

    @Composable
    fun Content() {
        Box(
            Modifier
                .focusRequester(backwardRequester)
                .onFocusChanged {
                    if (it.isFocused && !isRequesting) {
                        focusManager.clearFocus(force = true)
                    }
                }
                .focusTarget()
        )
        Box(
            Modifier
                .focusRequester(forwardRequester)
                .onFocusChanged {
                    if (it.isFocused && !isRequesting) {
                        focusManager.clearFocus(force = true)
                    }
                }
                .focusTarget()
        )
    }
}

external fun onGloballyPositioned(id:String,x: Float, y: Float,width: Float, height: Float)


val Document.nonNullBody: HTMLElement
    get() = document.body ?: error("null body")

var generateId: Int = 0
    get() {
        return ++field
    }


private fun initializingElement(id : String) : Unit = js("""
    {
        var box = document.getElementById(id);
        box.style.position = 'absolute';
        box.style.margin = '0px';
    }
""")


private fun changeCoordinates(id : String,width: Float,height: Float,x: Float,y: Float) : Unit = js("""
    {
        var box = document.getElementById(id);
        box.style.position = 'absolute';
        box.style.width = width + 'px';
        box.style.height = height + 'px';
        box.style.left = x + 'px';
        box.style.top = y + 'px';
    }
""")

@Composable
fun <T : Element> HtmlView(
    factory: () -> T,
    modifier: Modifier = Modifier,
    update: (T) -> Unit = NoOpUpdate
) {
    val componentInfo = remember { ComponentInfo<T>() }

    val root = LocalLayerContainer.current
    val density = LocalDensity.current.density
    val focusManager = LocalFocusManager.current
    val focusSwitcher = remember { FocusSwitcher(componentInfo, focusManager) }

    Box(
        modifier = modifier.onGloballyPositioned { coordinates ->
            val location = coordinates.positionInWindow().round()
            val size = coordinates.size
            changeCoordinates(componentInfo.component.id,size.width / density, size.height / density, location.x / density,location.y / density)
        }
    ) {
        focusSwitcher.Content()
    }

    DisposableEffect(factory) {
        componentInfo.container = root.appendElement("div", NoOpUpdate)
        componentInfo.component = factory()
        if(componentInfo.component.id.isEmpty()) componentInfo.component.id = generateId.toString()
        componentInfo.container.append(componentInfo.component)
        componentInfo.updater = Updater(componentInfo.component, update)
        initializingElement(id = componentInfo.component.id)
        onDispose {
            root.removeChild(componentInfo.container)
            componentInfo.updater.dispose()
        }
    }

    SideEffect {
        componentInfo.updater.update = update
    }
}


private class Updater<T : Element>(
    private val component: T,
    update: (T) -> Unit
) {
    private var isDisposed = false
    private val isUpdateScheduled = false
    private val snapshotObserver = SnapshotStateObserver { command ->
        command()
    }

    private val scheduleUpdate = { _: T ->
        if(isDisposed.not()) {
            performUpdate()
        }
        /*if (!isUpdateScheduled.getAndSet(true)) {
            SwingUtilities.invokeLater {
                isUpdateScheduled.set(false)
                if (!isDisposed) {
                    performUpdate()
                }
            }
        }*/
    }

    var update: (T) -> Unit = update
        set(value) {
            if (field != value) {
                field = value
                performUpdate()
            }
        }

    private fun performUpdate() {
        // don't replace scheduleUpdate by lambda reference,
        // scheduleUpdate should always be the same instance
        snapshotObserver.observeReads(component, scheduleUpdate) {
            update(component)
        }
    }

    init {
        snapshotObserver.start()
        performUpdate()
    }

    fun dispose() {
        snapshotObserver.stop()
        snapshotObserver.clear()
        isDisposed = true
    }
}