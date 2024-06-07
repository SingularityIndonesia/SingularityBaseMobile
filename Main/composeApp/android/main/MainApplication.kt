package main

import android.app.Application
import android.util.Log
import com.pluto.Pluto
import com.pluto.plugins.datastore.pref.PlutoDatastorePreferencesPlugin
import com.pluto.plugins.exceptions.PlutoExceptions
import com.pluto.plugins.exceptions.PlutoExceptionsPlugin
import com.pluto.plugins.logger.PlutoLoggerPlugin
import com.pluto.plugins.network.PlutoNetworkPlugin
import com.pluto.plugins.preferences.PlutoSharePreferencesPlugin
import com.pluto.plugins.rooms.db.PlutoRoomsDatabasePlugin
import com.singularity.basemobile.BuildConfig

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            applyPlutoExtension()
    }

    fun Application.applyPlutoExtension() {
        Pluto.Installer(application = this)
            .addPlugin(PlutoNetworkPlugin())
            .addPlugin(PlutoLoggerPlugin())
            .addPlugin(PlutoSharePreferencesPlugin())
            .addPlugin(PlutoExceptionsPlugin())
            .addPlugin(PlutoRoomsDatabasePlugin())
            .addPlugin(PlutoDatastorePreferencesPlugin())
            .install()

        PlutoExceptions.setExceptionHandler { thread, throwable ->
            Log.d(
                "Pluto exception report",
                "uncaught exception handled on thread: " + thread.name,
                throwable
            )
        }

        PlutoExceptions.setANRHandler { thread, exception ->
            Log.d(
                "Pluto exception report",
                "potential ANR detected on thread: " + thread.name,
                exception
            )
        }

        PlutoExceptions.mainThreadResponseThreshold = 10_000
    }
}