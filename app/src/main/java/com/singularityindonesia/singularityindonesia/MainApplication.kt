package com.singularityindonesia.singularityindonesia

import android.app.Application
import android.content.Context
import com.singularityindonesia.debugger.applyPlutoExtension
import com.singularityindonesia.main_context.MainContext

class MainApplication: Application() {

    override fun attachBaseContext(base: Context?) {
        base?.run(MainContext::init)
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        applyPlutoExtension()
    }

}