/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import example.model.TodoID
import example.presentation.ExampleTodoDetailScreen
import example.presentation.ExampleTodoDetailScreenPld
import example.presentation.ExampleTodoListScreen
import example.presentation.ExampleTodoListScreenPld


@Composable
fun ExampleNavigation() {
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
                pld = payload
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
                    id = userID
                )
            }

            ExampleTodoDetailScreen(
                pld = payload
            )
        }
    }
}