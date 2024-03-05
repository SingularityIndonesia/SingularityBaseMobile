package com.singularityindonesia.webrepository

import android.content.Context
import com.singularityindonesia.webrepository.util.createHttpClient
import io.ktor.client.*

interface WebRepositoryContext {
    val httpClient: HttpClient
}

class WebRepositoryContextDelegate(
    val context: Context,
): WebRepositoryContext {

    override val httpClient: HttpClient by lazy {
        createHttpClient()
    }
}
