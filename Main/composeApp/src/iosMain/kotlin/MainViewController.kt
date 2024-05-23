/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSProcessInfo
import system.core.context.MainContext
import system.core.context.WebRepositoryContext
import shared.webrepository.webRepositoryContext

fun MainViewController() = ComposeUIViewController {

    val env = remember {
        NSProcessInfo.processInfo.environment
    }

    val host = remember {
        env["HOST"].toString()
    }

    val basePath = remember {
        env["BASE_API_PATH"].toString()
    }

    val initMainContext = remember {
        MainContext.init(
            object : MainContext {
                override val webRepositoryContext: WebRepositoryContext by webRepositoryContext(
                    host = host,
                    basePath = basePath
                )
                /*override val storageContext: StorageContext by lazy { TODO() }*/
            }
        )
    }

    App()
}