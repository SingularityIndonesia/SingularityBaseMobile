package com.singularityindonesia.composeapp

import androidx.compose.runtime.Composable
import com.singularityindonesia.designsystem.theme.SingularityIndonesiaTheme

@Composable
fun ExampleApp() {
    SingularityIndonesiaTheme {
        // A surface container using the 'background' color from the theme
        ExampleNavigation()
    }
}