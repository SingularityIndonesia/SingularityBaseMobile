/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package shared.webrepository

import system.core.context.WebRepositoryContext
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

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
