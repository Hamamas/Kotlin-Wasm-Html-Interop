import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        var count by remember { mutableIntStateOf(0) }
        Column {
            Row {

                HtmlView(
                    modifier = Modifier.weight(1f).height(100.dp).background(randomColor),
                    factory = {
                        document.createElement("h1")
                    },
                    update = {
                        it.textContent = "count is $count"
                    }
                )

                Box(modifier = Modifier.weight(1f).height(100.dp).background(randomColor), contentAlignment = Alignment.Center) {
                    Text(
                        text = count.toString(),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 24.sp
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { count++ }) {
                    Text("++")
                }
                Button(onClick = { count-- }) {
                    Text("--")
                }
            }
        }
    }
}