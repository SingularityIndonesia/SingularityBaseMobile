import plugin.convention.companion.System

plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
    id("FeaturePane")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            System("designsystem")
        }
    }
}

android {
    namespace = "shared.bottomsheetflow"
}

task("testClasses")
