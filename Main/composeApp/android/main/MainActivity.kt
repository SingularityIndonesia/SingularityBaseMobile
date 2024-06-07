/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SetStatusBarColor()
            App()
        }
    }

    @Composable
    private fun SetStatusBarColor() {

        @Suppress("DEPRECATION")
        val darkDecorView = window.decorView.systemUiVisibility

        @Suppress("DEPRECATION")
        val lightDecorView = window.decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

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
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}