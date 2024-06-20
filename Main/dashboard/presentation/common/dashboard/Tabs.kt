package dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import common.StateSaver
import example.model.TodoID
import example.presentation.TodoListPane
import example.presentation.TodoListPanePld

@Composable
fun Context.DashboardTabNavigation(
    navController: NavHostController,
    stateSaver: StateSaver,
    goToTodoDetail: (TodoID) -> Unit,
) {

    NavHost(
        navController = navController,
        startDestination = "prolog",
    ) {

        composable(
            route = "prolog",
        ) {
            PrologPane()
        }

        composable(
            route = "todo-list",
        ) { backstackEntry ->

            val payload = remember { TodoListPanePld() }
            with(exampleContext) {
                TodoListPane(
                    pld = payload,
                    stateSaver = stateSaver,
                    goToTodoDetail = goToTodoDetail,
                )
            }
        }
    }
}