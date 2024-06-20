import plugin.convention.companion.Shared
import plugin.convention.companion.System

plugins {
    id("LibraryConventionV2")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
    id("FeatureSerialization")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            System("core")
            Shared("common")
            Shared("gemini")
        }
    }
}
android {
    namespace = "main.ai_chat.model"
}

task("testClasses")