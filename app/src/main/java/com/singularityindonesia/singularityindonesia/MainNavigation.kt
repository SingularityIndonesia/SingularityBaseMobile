package com.singularityindonesia.singularityindonesia

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.singularityindonesia.screen.TodoListScreen


@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "todo-list"
    ) {

        composable(
            route = "todo-list",
        ) {
            TodoListScreen()
        }
    }
}