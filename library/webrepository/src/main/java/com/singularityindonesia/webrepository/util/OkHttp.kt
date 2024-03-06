package com.singularityindonesia.webrepository.util

import io.ktor.client.*
import okhttp3.OkHttpClient

fun createHttpClient(
    builders: List<HttpClientConfig<*>.() -> Unit> = listOf()
) = HttpClient {
    builders.forEach {
        it.invoke(this)
    }
}