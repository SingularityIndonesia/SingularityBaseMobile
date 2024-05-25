/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("FeatureJetpackCompose")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {

            implementation(libs.kotlinx.serialization.json)

            implementation("system:core")
            implementation("system:designsystem")
            implementation("shared:common")

            implementation(project(":ex_apigenerator:data"))
            implementation(project(":ex_apigenerator:model"))
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
    }
}

android {
    namespace = "main.ex_apigenerator.presentation"
    dependencies {
      debugImplementation(libs.compose.ui.tooling)
    }
}

task("testClasses")
