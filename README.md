This is a Kotlin Multiplatform project targeting Web.

the main purpose of the project is make the HTML and COMPOSE WEB interop


**AnimatedVisibility**

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
                        val video = document.createElement("video")
                        video.setAttribute(
                            "src",
                            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                        )
                        video
                    }
                )
            }
        }
![image description](screenshots/animatedVisibility.png)
**LazyColumn**

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
![image description](screenshots/lazyColumn.png)
**LazyRow**

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
![image description](screenshots/lazyRow.png)



