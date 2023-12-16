import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.browser.document
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun App() {
    MaterialTheme {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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