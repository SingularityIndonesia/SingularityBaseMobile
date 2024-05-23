/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import plugin.convention.companion.env
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("AppConventionV1")
    id("FeatureJetpackCompose")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.compose.navigation)

            implementation("system:core")
            implementation("system:designsystem")
            implementation("shared:common")
            implementation("shared:webrepository")

            implementation(project(":example:presentation"))
            implementation(project(":example:model"))
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
    }
}

android {
    namespace = "com.singularity.basemobile"
    defaultConfig {
        applicationId = "com.singularity.basemobile"
        versionCode = 1
        versionName = "1.0"
    }

    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}


android.buildTypes {
    getByName("devDebug") {
        buildConfigField(
            "String",
            "HOST",
            env.getProperty("DEV_HOST")
        )
        buildConfigField(
            "String",
            "API_BASE_PATH",
            env.getProperty("DEV_API_BASE_PATH")
        )
    }

    getByName("devRelease") {
        buildConfigField(
            "String",
            "HOST",
            env.getProperty("DEV_HOST")
        )
        buildConfigField(
            "String",
            "API_BASE_PATH",
            env.getProperty("DEV_API_BASE_PATH")
        )
    }

    getByName("stagingDebug") {
        buildConfigField(
            "String",
            "HOST",
            env.getProperty("STAGE_HOST")
        )
        buildConfigField(
            "String",
            "API_BASE_PATH",
            env.getProperty("STAGE_API_BASE_PATH")
        )
    }

    getByName("stagingRelease") {
        buildConfigField(
            "String",
            "HOST",
            env.getProperty("STAGE_HOST")
        )
        buildConfigField(
            "String",
            "API_BASE_PATH",
            env.getProperty("STAGE_API_BASE_PATH")
        )
    }

    getByName("prodDebug") {
        buildConfigField(
            "String",
            "HOST",
            env.getProperty("PROD_HOST")
        )
        buildConfigField(
            "String",
            "API_BASE_PATH",
            env.getProperty("PROD_API_BASE_PATH")
        )
    }

    getByName("prodRelease") {
        buildConfigField(
            "String",
            "HOST",
            env.getProperty("PROD_HOST")
        )
        buildConfigField(
            "String",
            "API_BASE_PATH",
            env.getProperty("PROD_API_BASE_PATH")
        )
    }
}

task("testClasses")