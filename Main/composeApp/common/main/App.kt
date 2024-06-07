/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.getPlatform
import common.isIOS
import designsystem.SingularityTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    val context = remember { MainContext() }

    val topPadding = WindowInsets.safeDrawing
        .asPaddingValues()
        .calculateTopPadding()
        .let {
            if (getPlatform().isIOS())
                it.minus(8.dp)
            else
                it.minus(0.dp)
        }

    SingularityTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .padding(top = topPadding)
                    .imePadding()
            ) {
                with(context) {
                    MainNavigation()
                }
            }
        }
    }
}