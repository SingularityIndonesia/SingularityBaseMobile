package system.core.context

import io.ktor.client.*

interface WebRepositoryContext {
    val httpClient: HttpClient
    fun interceptBuilder(
        httpClientBuilder: HttpClientConfig<*>.() -> Unit
    )
}