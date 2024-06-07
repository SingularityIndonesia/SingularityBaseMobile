/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package example.data

import example.model.Context
import example.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


suspend fun Context.GetTodos(): Result<List<Todo>> =
    withContext(Dispatchers.Default) {
        webRepositoryContext.webClient.get("todos/")
            .map {
                val stringResponse = it.invoke().decodeToString()
                Json.decodeFromString<List<Todo>>(stringResponse)
            }
    }