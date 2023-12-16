This is a Kotlin Multiplatform project targeting Web.

`
HtmlView(
  modifier = Modifier.weight(1f).height(100.dp).background(randomColor),
  factory = {
    document.createElement("h1")
  },
  update = {
    it.textContent = "count is $count"
  }
)
`

