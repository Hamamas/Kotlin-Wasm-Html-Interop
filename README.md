This is a Kotlin Multiplatform project targeting Web.



how it works i create a box compostable inside canvas when the box moves or change his size i update the dom with current position or size of the box 

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

