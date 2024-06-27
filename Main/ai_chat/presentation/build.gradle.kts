import plugin.convention.companion.Shared
import plugin.convention.companion.System
import plugin.convention.companion.data
import plugin.convention.companion.model

plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
    id("FeaturePane")
    id("FeatureSerialization")
    id("FeatureHttpClient")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            System("core")
            System("designsystem")
            Shared("common")
            Shared("gemini")

            data("ai_chat")
            model("ai_chat")
        }
    }
}

android {

    namespace = "main.ai_chat.presentation"
}

task("testClasses")
