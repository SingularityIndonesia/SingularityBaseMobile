/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.composeapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.singularityindonesia.screen.todolist.TodoListScreen


@Composable
fun ExampleNavigation() {
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