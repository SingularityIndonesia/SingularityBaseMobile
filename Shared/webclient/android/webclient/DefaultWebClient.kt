package webclient

import com.pluto.plugins.network.ktor.PlutoKtorInterceptor
import core.context.WebClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.readBytes
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

actual fun defaultWebClient(
    host: String,
    basePath: String
): WebClient = object : WebClient {

    override val host: String = host

    override val basePath: String = basePath


    val ktorHttpClient by lazy {
        HttpClient {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    this.host = host
                    path(basePath)
                }
            }
            install(PlutoKtorInterceptor)
        }
    }

    override suspend fun post(
        endpoint: String?,
        headers: Map<String, String>?,
        params: Map<String, String>?,
        body: String?
    ): Result<suspend () -> ByteArray> {
        return runCatching {
            val requestBuilder: HttpRequestBuilder.() -> Unit = {
                if (!headers.isNullOrEmpty())
                    headers.forEach {
                        header(it.key, it.value)
                    }

                if (!params.isNullOrEmpty())
                    params.forEach {
                        parameter(it.key, it.value)
                    }

                if (!body.isNullOrBlank()) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
            }

            val response =
                if (endpoint.isNullOrBlank())
                    ktorHttpClient.post(requestBuilder)
                else
                    ktorHttpClient.post(endpoint, requestBuilder)

            return@runCatching suspend { response.readBytes() }
        }
    }

    override suspend fun get(
        endpoint: String?,
        headers: Map<String, String>?,
        params: Map<String, String>?
    ): Result<suspend () -> ByteArray> {
        return runCatching {
            val requestBuilder: HttpRequestBuilder.() -> Unit = {
                if (!headers.isNullOrEmpty())
                    headers.forEach {
                        header(it.key, it.value)
                    }

                if (!params.isNullOrEmpty())
                    params.forEach {
                        parameter(it.key, it.value)
                    }
            }

            val response =
                if (endpoint.isNullOrBlank())
                    ktorHttpClient.get(requestBuilder)
                else
                    ktorHttpClient.get(endpoint, requestBuilder)

            return@runCatching suspend { response.readBytes() }
        }
    }

    override suspend fun put(
        endpoint: String?,
        headers: Map<String, String>?,
        params: Map<String, String>?,
        body: String?
    ): Result<suspend () -> ByteArray> {
        return runCatching {
            val requestBuilder: HttpRequestBuilder.() -> Unit = {
                if (!headers.isNullOrEmpty())
                    headers.forEach {
                        header(it.key, it.value)
                    }

                if (!params.isNullOrEmpty())
                    params.forEach {
                        parameter(it.key, it.value)
                    }

                if (!body.isNullOrBlank()) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
            }

            val response =
                if (endpoint.isNullOrBlank())
                    ktorHttpClient.put(requestBuilder)
                else
                    ktorHttpClient.put(endpoint, requestBuilder)

            return@runCatching suspend { response.readBytes() }
        }
    }
}