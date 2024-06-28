import plugin.convention.companion.Shared
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
            Shared("simpleactivity")
        }
    }
}

android {
    namespace = "shared.bottomsheetflow"
}

task("testClasses")
