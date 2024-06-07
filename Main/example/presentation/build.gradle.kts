import plugin.convention.companion.Shared
import plugin.convention.companion.System
import plugin.convention.companion.data
import plugin.convention.companion.model

/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    id("LibraryConventionV2")
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

            data("example")
            model("example")
        }
    }
}

android {
    namespace = "main.example.presentation"
}

task("testClasses")
