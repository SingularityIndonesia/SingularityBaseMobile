package main

import com.singularity.basemobile.BuildConfig

actual data object EnvironmentVariables {
    actual val webHost: String = BuildConfig.HOST
    actual val todoBasePath: String = BuildConfig.TODO_API_BASE_PATH
    actual val geminiApiKey: String = BuildConfig.GEMINI_API_KEY
}