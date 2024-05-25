/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("FeatureJetpackCompose")
}

kotlin {
    sourceSets {

    }
}

android {
    namespace = "system.designsystem"
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

task("testClasses")