/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.main_context

import com.singularityindonesia.exception.MUnHandledException

interface MainContext {
    val webRepositoryContext: WebRepositoryContext
    val storageContext: StorageContext

    companion object {
        lateinit var mainContext: MainContext
        fun init(context: MainContext) {
            mainContext = context
        }

        inline operator fun <reified T> MainContext.invoke(): T =
            when (T::class.java) {
                WebRepositoryContext::class.java -> mainContext.webRepositoryContext as T
                StorageContext::class.java -> mainContext.storageContext as T
                else -> throw MUnHandledException("Unknown context ${T::class.simpleName}")
            }
    }
}

