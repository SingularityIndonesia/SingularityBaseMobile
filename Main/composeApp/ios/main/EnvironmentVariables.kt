package main

import platform.Foundation.NSProcessInfo

private val env by lazy {
    NSProcessInfo.processInfo.environment
}

actual data object EnvironmentVariables {
    actual val webHost: String = env["HOST"].toString()
    actual val todoApiBasePath: String = env["TODO_API_BASE_PATH"].toString()
    actual val geminiApiKey: String = env["GEMINI_API_KEY"].toString()
}