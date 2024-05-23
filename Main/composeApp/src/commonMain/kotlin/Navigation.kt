/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import system.core.lifecycle.SaveAbleState
import main.example.model.TodoID
import main.example.presentation.ExampleTodoDetailScreen
import main.example.presentation.ExampleTodoDetailScreenPld
import main.example.presentation.ExampleTodoListScreen
import main.example.presentation.ExampleTodoListScreenPld


@Composable
fun ExampleNavigation() {
    val saveAbleState = remember { SaveAbleState() }
    val navController = rememberNavController()

    val goToTodoDetail = remember {
        { todoID: TodoID ->
            navController.navigate("todo-detail/${todoID.value}")
        }
    }

    NavHost(
        navController = navController,
        startDestination = "todo-list"
    ) {

        composable(
            route = "todo-list",
        ) { backstackEntry ->

            val payload = remember {
                ExampleTodoListScreenPld(
                    goToTodoDetail = goToTodoDetail
                )
            }

            ExampleTodoListScreen(
                pld = payload,
                saveAbleState = saveAbleState
            )
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
                ExampleTodoDetailScreenPld(
                    id = userID,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            ExampleTodoDetailScreen(
                pld = payload,
                saveAbleState = saveAbleState
            )
        }
    }
}