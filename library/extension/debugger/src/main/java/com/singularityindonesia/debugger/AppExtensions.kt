package com.singularityindonesia.debugger

import android.app.Application
import com.pluto.BuildConfig
import com.pluto.Pluto
import com.pluto.plugins.datastore.pref.PlutoDatastorePreferencesPlugin
import com.pluto.plugins.exceptions.PlutoExceptionsPlugin
import com.pluto.plugins.logger.PlutoLoggerPlugin
import com.pluto.plugins.network.PlutoNetworkPlugin
import com.pluto.plugins.network.ktor.PlutoKtorInterceptor
import com.pluto.plugins.preferences.PlutoSharePreferencesPlugin
import com.pluto.plugins.rooms.db.PlutoRoomsDatabasePlugin
import com.singularityindonesia.main_context.MainContext
import com.singularityindonesia.webrepository.WebRepositoryContext

fun Application.applyPlutoExtension() {
    Pluto.Installer(this)
        .addPlugin(PlutoNetworkPlugin())
        .addPlugin(PlutoLoggerPlugin())
        .addPlugin(PlutoSharePreferencesPlugin())
        .addPlugin(PlutoExceptionsPlugin())
        .addPlugin(PlutoRoomsDatabasePlugin())
        .addPlugin(PlutoDatastorePreferencesPlugin())
        .install()

    also {
        if (BuildConfig.DEBUG)
            (MainContext.get() as WebRepositoryContext)
                .interceptBuilder {
                    install(PlutoKtorInterceptor)
                }
    }
}