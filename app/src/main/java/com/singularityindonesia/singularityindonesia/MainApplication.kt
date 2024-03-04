package com.singularityindonesia.singularityindonesia

import android.app.Application
import android.content.Context

class MainApplication: Application() {

    private val mainContext: MainContext by lazy {
        MainContextDelegate(context = baseContext)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

    }

    override fun getBaseContext(): Context {
        return mainContext.context
    }


}