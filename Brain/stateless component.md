Means that a component and its states must be separated.

❌ **stateful component:**
```kotlin
@Composable
fun PrimaryButton(
	onClick: () -> Unit
) {
	var isLoading by remember { mutableStateOf(false) }
	Button(
		onClick: {
			isLoading = true
			onClick.invoke()
		}
	){
		if(isLoading)
			CircularPrograssIndicator()
		else
			Text(label)
	}
}
```
The example above is a stateful widget because the button have a state in it.

✅ **stateless component**
```kotlin
@Composable
fun PrimaryButton(
	label: String,
	isLoading: Boolean,
	onClick: () -> Unit
) {
	var isLoading by remember { mutableStateOf(false) }
	Button(
		onClick: onClick
	) {
		if(isLoading)
			CircularPrograssIndicator()
		else
			Text(label)
	}
}
```
The example above is a stateless widget because its state outside the component. By this way, the parent component can easily intercept the `PrimaryButton's` state.

Related topic:
- [[event propagation]]
