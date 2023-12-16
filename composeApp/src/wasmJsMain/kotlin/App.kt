import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.browser.document

@Composable
fun App() {
    MaterialTheme {
        LazyRow(modifier = Modifier.fillMaxSize()) {
            items(50) {
                if (it % 2 < 1) {
                    HtmlView(
                        modifier = Modifier.size(100.dp).background(randomColor),
                        factory = {
                            val element = document.createElement("h3")
                            element.textContent = "index $it"
                            element
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.size(100.dp).background(randomColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "index $it")
                    }
                }
            }
        }
    }
}