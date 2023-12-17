package examples

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hamama.kwhi.HtmlView
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.appendElement
import org.w3c.dom.Attr


@Composable
fun App() {

    MaterialTheme {

        var visibility by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(onClick = { visibility = visibility.not() }) {
                Text(if (visibility) "Hide video" else "Show video")
            }

            AnimatedVisibility(visibility) {
                HtmlView(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    factory = {
                        val video = createElement("video")
                        video.setAttribute("controls","")
                        video.setAttribute("preload","auto")
                        video.setAttribute("data-setup","{}")
                        video.appendElement("source") {
                            setAttribute("src","http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                            setAttribute("type","video/mp4")
                        }
                        video.className = "video-js"
                        video
                    }
                )
            }

        }
    }

}