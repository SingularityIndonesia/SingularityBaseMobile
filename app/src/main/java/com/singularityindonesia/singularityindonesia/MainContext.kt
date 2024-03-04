package com.singularityindonesia.singularityindonesia

import android.app.Application
import android.content.Context
import com.singularityindonesia.webrepository.WebRepositoryContext
import com.singularityindonesia.webrepository.WebRepositoryContextDelegate

interface MainContext : WebRepositoryContext {
    val context: Context
}

class MainContextDelegate(
    override val context: Context
) : MainContext, WebRepositoryContext by WebRepositoryContextDelegate(context)

