/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.webrepository

import com.singularityindonesia.model.Todo
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

context(WebRepositoryContext)
suspend fun GetTodos(): Result<List<Todo>> = withContext(Dispatchers.IO) {
    kotlin.runCatching {

        val response = httpClient.get("https://jsonplaceholder.typicode.com/todos/")

        response
            .bodyAsText()
            .let {
                Json.decodeFromString<List<Todo>>(it)
            }
    }
}