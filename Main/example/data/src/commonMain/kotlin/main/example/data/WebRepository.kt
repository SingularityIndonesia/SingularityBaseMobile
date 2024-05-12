package main.example.data

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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

        val response = httpClient.get("https://jsonplaceholder.typicode.com/todos/")

        response
            .bodyAsText()
            .let {
                Json.decodeFromString<List<Todo>>(it)
            }
    }
}