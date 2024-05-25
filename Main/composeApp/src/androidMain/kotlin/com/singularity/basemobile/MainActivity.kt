/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package com.singularity.basemobile

import App
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import shared.webrepository.webRepositoryContext
import system.core.context.MainContext
import system.core.context.WebRepositoryContext

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(base: Context?) {
        if (base == null)
            return

        MainContext.init(
            object : MainContext {
                override val webRepositoryContext: WebRepositoryContext by webRepositoryContext(
                    host = BuildConfig.HOST,
                    basePath = BuildConfig.API_BASE_PATH
                )
                /*override val storageContext: StorageContext by storageContext(base)*/
            }
        )
        super.attachBaseContext(base)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val darkDecorView = window.decorView.systemUiVisibility
        val lightDecorView = window.decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContent {
            val isDarkTheme = isSystemInDarkTheme()

            with(window) {
                statusBarColor = Color.TRANSPARENT

                decorView.systemUiVisibility = if (isDarkTheme)
                    darkDecorView
                else
                    lightDecorView

                WindowCompat.setDecorFitsSystemWindows(
                    window,
                    false
                )
            }

            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}