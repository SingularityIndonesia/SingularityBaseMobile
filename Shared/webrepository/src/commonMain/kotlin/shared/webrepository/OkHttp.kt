/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
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