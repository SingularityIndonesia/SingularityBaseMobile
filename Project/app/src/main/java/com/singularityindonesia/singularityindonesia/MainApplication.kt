/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.singularityindonesia

import android.app.Application
import android.content.Context
import com.singularityindonesia.debugger.applyPlutoExtension
import com.singularityindonesia.main_context.MainContext

class MainApplication: Application() {

    override fun attachBaseContext(base: Context?) {
        base?.let(MainContext::init)
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        applyPlutoExtension()
    }

}