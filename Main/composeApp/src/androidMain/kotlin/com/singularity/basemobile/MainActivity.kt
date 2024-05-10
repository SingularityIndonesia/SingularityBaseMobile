package com.singularity.basemobile

import App
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.singularity.contex.MainContext
import com.singularity.contex.WebRepositoryContext
import com.singularity.webrepository.webRepositoryContext

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(base: Context?) {
        if (base == null)
            return

        MainContext.init(
            object : MainContext {
                override val webRepositoryContext: WebRepositoryContext by webRepositoryContext()
                /*override val storageContext: StorageContext by storageContext(base)*/
            }
        )
        super.attachBaseContext(base)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}