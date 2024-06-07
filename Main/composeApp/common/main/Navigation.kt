/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package main

import ai_chat.AIChatPane
import ai_chat.AIChatPanePld
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import core.lifecycle.StateSaver
import dashboard.DashboardPane
import example.presentation.TodoDetailPane
import example.presentation.TodoDetailPanePld


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