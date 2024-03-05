package com.singularityindonesia.main_context

import android.content.Context
import com.singularityindonesia.webrepository.WebRepositoryContext
import com.singularityindonesia.webrepository.WebRepositoryContextDelegate

interface MainContext : WebRepositoryContext {
    companion object {
        private var mainContext: MainContext? = null

        fun init(context: Context) {
            mainContext =
                object : MainContext, WebRepositoryContext by WebRepositoryContextDelegate(context) {
                    override val context: Context = context
                }
        }

        fun get(): MainContext {
            return mainContext?: run { throw NullPointerException("MainContext is not yet initialized.") }
        }
    }

    val context: Context
}

class MainContextDelegate(
    override val context: Context
) : MainContext, WebRepositoryContext by WebRepositoryContextDelegate(context)

