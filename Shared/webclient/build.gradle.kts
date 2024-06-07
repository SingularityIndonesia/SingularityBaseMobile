import plugin.convention.companion.System

plugins {
    id("LibraryConventionV2")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
    id("FeatureHttpClient")
    id("FeatureAndroidPluto")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            System("core")
        }
    }
}

android {
    namespace = "shared.webclient"
}

task("testClasses")
