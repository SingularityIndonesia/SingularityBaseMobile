There is nothing crazy in this codebase.
Navigation is a compositions or a collection of [[pane]]s; `Nav = Collection<Pane>`.

```kotlin
@Composable
fun MainContext.MainNavigation() {
    val stateSaver = remember { StateSaver() }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {

        composable(
            route = "dashboard",
        ) {
            with(dashboardContext) {
                DashboardPane(
                    gotoGroot = {
                        navController.navigate("groot")
                    },
                    gotoTodoDetail = {
                        navController.navigate("todo-detail/${it.value}")
                    }
                )
            }
        }

        composable(
            route = "groot"
        ) {
            val payload = remember {
                AIChatPanePld()
            }

            with(dashboardContext.aiChatContext) {
                AIChatPane(
                    pld = payload,
                    stateSaver = stateSaver,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            route = "todo-detail/{todoID}",
            arguments = listOf(
                navArgument("todoID") {
                    type = NavType.StringType
                }
            )
        ) { backstackEntry ->

            val userID = backstackEntry.arguments
                ?.getString("todoID")
                ?: throw Error("I'm too lazy to handle error.")

            val payload = remember(userID) {
                TodoDetailPanePld(
                    id = userID,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            with(exampleContext) {
                TodoDetailPane(
                    pld = payload,
                    stateSaver = stateSaver
                )
            }
        }

    }
}
```
