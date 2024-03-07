/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
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