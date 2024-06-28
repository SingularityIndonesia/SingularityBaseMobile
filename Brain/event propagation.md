Example scenario, a button deep inside the widget change the background color with cloud syncronization:
```kotlin
@Composable
fun SomePane() {
	val viewmodel: SomeViewModel = viewModel()

	...
	val backgroundColor 
		by viewmodel.backgroundColor.collectAsState()
		
	Background(
		color: viewmodel.
	)

	...
	val isLoadingChangeBackground 
		by viewmodel.isLoadingChangeBackground.collectAsState()
		
	SomeWidget(
		isLoadingChangeBackground = isLoadingChangeBackground,
		onChangeBackground = {
			// doing cloud sync
			viewmodel.changeBackground()
		}
	)
	...
}

@Composable
fun SomeWidget(
	isLoadingChangeBackground: Boolean,
	onChangeBackground: () -> Unit
) {
	...
	ButtonWithLoading(
		label = "Change Background Color",
		isLoading = isLoadingChangeBackground,
		onClick = onChangeBackground
	)
	...
}

@Composable
fun ButtonWithLoading(
	label: String,
	isLoading: Boolean,
	onClick: () -> Unit
) {
	Button(
		onClick: onClick
	) {
		if(isLoading)
			CircularProgressIndicator()
		else
			Text(label)
	}
}
```

Problem explanation:
- `viewmodel.changeBackground()` should be done by `SomePane` because viewmodel is inside the `SomePane`'s scope.
- **You cannot parse the viewmodel** into the `SomeWidget`, you simply cannot parse it to any widget - Because by doing so, we cannot know who will trigger the viewmodel action, the `SomeWidget` probably will parse the viewmodel to someone else, who knows?
- Since only the `SomePane` can see the viewmodel and do side effect to the viewmodel, **`ButtonWithLoading` need the `SomePane` to tell him when to change its state to loading**.
- **The `ButtonWithLoading` cannot trigger the viewmodel action** since the viewmodel is beyond its authority. 

Solution explanation:
- The `ButtonWithLoading` **propagate** the `onClick` event to the parent component which is the `SomeWidget`.
- The `SomeWidget` then **propagate** the `onClick` event to the `SomePane` via `onChangeBackground` event.
- The `SomePane` then receive the **propagated** event from the `ButtonWithLoading` component.

Related:
- [[centralized context control]]