import plugin.convention.companion.Shared
import plugin.convention.companion.System
import plugin.convention.companion.data
import plugin.convention.companion.model

plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    // id("CompileWasm")
    id("FeatureCoroutine")
    id("FeaturePane")
    id("FeatureSerialization")
    id("FeatureHttpClient")
    id("FeatureContextReceiver")
    id("FeatureOptic")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            System("core")
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

dependencies {
    // ... but instead: here!
//    add("kspCommonMainMetadata", libs.some.ksp.plugin) // Run KSP on [commonMain] code
    add("kspAndroid", libs.arrow.ksp.plugin)
//    add("kspIosX64", libs.some.ksp.plugin)
//    add("kspIosArm64", libs.some.ksp.plugin)
//    add("kspIosSimulatorArm64", libs.some.ksp.plugin)
}

task("testClasses")
