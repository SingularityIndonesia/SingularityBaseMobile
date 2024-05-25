/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package shared.webrepository

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import system.core.context.WebRepositoryContext

fun webRepositoryContext(
    host: String,
    basePath: String,
): Lazy<WebRepositoryContext> =
    lazy {
        object : WebRepositoryContext {
            private val builders = mutableListOf<HttpClientConfig<*>.() -> Unit>()

            override val httpClient: HttpClient by lazy {
                createHttpClient(
                    host = host,
                    basePath = basePath,
                    builders = builders
                )
            }

            override fun interceptBuilder(
                httpClientBuilder: HttpClientConfig<*>.() -> Unit
            ) {
                builders.add(httpClientBuilder)
            }

        }
    }
