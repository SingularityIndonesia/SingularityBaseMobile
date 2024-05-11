/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import example.presentation.ExampleTodoListScreen


@Composable
fun ExampleNavigation() {
    var destination by remember {
        mutableStateOf(
            "todo-list"
        )
    }

    when(destination) {
        "todo-list" -> {
            ExampleTodoListScreen()
        }
    }
}