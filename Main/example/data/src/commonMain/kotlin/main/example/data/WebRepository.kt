/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package main.example.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import main.example.model.Todo

// BUG, context receiver implementation is still not working,
// so for now you need to use basic composition pattern.
//context(WebRepositoryContext)
//suspend fun GetTodos(): Result<List<Todo>> = withContext(Dispatchers.IO) {
//    kotlin.runCatching {
//
//        val response = httpClient.get("https://jsonplaceholder.typicode.com/todos/")
//
//        response
//            .bodyAsText()
//            .let {
//                Json.decodeFromString<List<Todo>>(it)
//            }
//    }
//}

suspend fun GetTodos(
    httpClient: HttpClient
): Result<List<Todo>> = withContext(Dispatchers.IO) {
    kotlin.runCatching {

        val response = httpClient
            .get("todos/")

        response
            .bodyAsText()
            .let {
                Json.decodeFromString<List<Todo>>(it)
            }
    }
}