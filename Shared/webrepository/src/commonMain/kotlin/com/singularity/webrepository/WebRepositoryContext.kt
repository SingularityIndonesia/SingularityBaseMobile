/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularity.webrepository

import com.singularity.contex.WebRepositoryContext
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

fun webRepositoryContext(): Lazy<WebRepositoryContext> =
    lazy {
        object : WebRepositoryContext {
            private val builders = mutableListOf<HttpClientConfig<*>.() -> Unit>()

            override val httpClient: HttpClient by lazy {
                createHttpClient(
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
