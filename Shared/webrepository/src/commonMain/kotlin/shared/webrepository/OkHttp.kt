/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package shared.webrepository

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.http.path

fun createHttpClient(
    host: String,
    basePath: String,
    builders: List<HttpClientConfig<*>.() -> Unit> = listOf()
) = HttpClient {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            this.host = host
            path(basePath)
        }
    }
    builders.forEach {
        it.invoke(this)
    }
}