plugins {
    id("LibraryConventionV2")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.generativeai)
        }
        iosMain.dependencies {
            implementation(libs.generativeai)
        }
    }
}

android {
    namespace = "shared.gemini"
}

task("testClasses")

