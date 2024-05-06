/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.singularityindonesia

import android.app.Application
import android.content.Context
import com.singularityindonesia.debugger.applyPlutoPlugin
import com.singularityindonesia.main_context.MainContext
import com.singularityindonesia.main_context.StorageContext
import com.singularityindonesia.main_context.WebRepositoryContext
import com.singularityindonesia.storage.storageContext
import com.singularityindonesia.webrepository.webRepositoryContext

class MainApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        if (base == null)
            return

        MainContext.init(
            object : MainContext {
                override val webRepositoryContext: WebRepositoryContext by webRepositoryContext()
                override val storageContext: StorageContext by storageContext(base)
            }
        )
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
//        applyPlutoPlugin()
    }

}