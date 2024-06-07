import plugin.convention.companion.model

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
            model("ai_chat")
            model("example")
        }
    }
}

android {
    namespace = "main.dashboard.model"
}

task("testClasses")
