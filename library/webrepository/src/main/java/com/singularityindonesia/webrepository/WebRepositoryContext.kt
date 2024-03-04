package com.singularityindonesia.webrepository

import android.app.Application
import android.content.Context
import com.singularityindonesia.webrepository.util.createOkHttpClient
import okhttp3.OkHttpClient

interface WebRepositoryContext {
    val okHttpClient: OkHttpClient
}

class WebRepositoryContextDelegate(
    val context: Context,
): WebRepositoryContext {

    override val okHttpClient: OkHttpClient by lazy {
        createOkHttpClient()
    }
}
