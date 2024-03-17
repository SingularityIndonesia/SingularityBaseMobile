/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.main_context

import android.annotation.SuppressLint
import android.content.Context
import com.singularityindonesia.exception.MUnHandledException
import com.singularityindonesia.webrepository.WebRepositoryContext
import com.singularityindonesia.webrepository.webRepositoryContext

@SuppressLint("StaticFieldLeak")
object MainContext {

    @JvmStatic
    lateinit var context: Context

    val webRepositoryContext by webRepositoryContext()

    fun init(context: Context) {
        this.context = context
    }

    inline operator fun <reified T> invoke(): T =
        when (T::class.java) {
            WebRepositoryContext::class.java -> webRepositoryContext
            else -> null
        }
            ?.let { it as T }
            ?: throw MUnHandledException("Unknown context ${T::class.simpleName}")

}

