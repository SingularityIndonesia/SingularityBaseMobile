package com.singularityindonesia.singularityindonesia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.singularityindonesia.designsystem.theme.SingularityIndonesiaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SingularityIndonesiaTheme {
                // A surface container using the 'background' color from the theme
                MainNavigation()
            }
        }
    }
}