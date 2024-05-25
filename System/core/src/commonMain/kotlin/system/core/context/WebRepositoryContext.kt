/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package system.core.context

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

interface WebRepositoryContext {
    val httpClient: HttpClient
    fun interceptBuilder(
        httpClientBuilder: HttpClientConfig<*>.() -> Unit
    )
}