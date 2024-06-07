import org.jetbrains.compose.ComposeExtension
import plugin.convention.companion.Shared
import plugin.convention.companion.System
import plugin.convention.companion.data
import plugin.convention.companion.model
import plugin.convention.companion.presentation

plugins {
    id("LibraryConventionV2")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
    id("FeaturePane")
    id("FeatureNavigation")
    id("FeatureSerialization")
    id("FeatureHttpClient")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            System("core")
            System("designsystem")
            Shared("common")

            data("dashboard")
            model("dashboard")

            model("ai_chat")
            presentation("ai_chat")
            model("example")
            presentation("example")
        }
    }
}

android {

    namespace = "main.dashboard.presentation"

    buildFeatures {
        compose = true
    }
}

task("testClasses")
