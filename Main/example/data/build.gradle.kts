import plugin.convention.companion.Shared
import plugin.convention.companion.System
import plugin.convention.companion.model

/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
    id("FeatureHttpClient")
    id("FeatureSerialization")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            System("core")
            Shared("common")

            model("example")
        }
    }
}


android {
    namespace = "main.example.data"
}

task("testClasses")
