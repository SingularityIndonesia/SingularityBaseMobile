/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
plugins {
    `kotlin-dsl`
}

group = "com.singularityindonesia.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    implementation("com.android.tools.build:gradle:8.2.0")
    /*implementation(libs.ksp.gradle.plugin)
    implementation(libs.detekt.plugin)
    implementation(libs.javapoet.plugin)
    implementation(libs.navigation.safeargs.plugin)
    implementation(libs.kotlinx.kover.plugin)*/
}

gradlePlugin {
    plugins {
        register("AppConventionV1") {
            id = "AppConventionV1"
            implementationClass = "com.singularityindonesia.convention.AppConventionV1"
        }
        register("LibraryConventionV1") {
            id = "LibraryConventionV1"
            implementationClass = "com.singularityindonesia.convention.LibraryConventionV1"
        }
        register("FeatureJetpackCompose") {
            id = "FeatureJetpackCompose"
            implementationClass = "com.singularityindonesia.convention.features.FeatureJetpackCompose"
        }
        register("FeatureContextReceiver") {
            id = "FeatureContextReceiver"
            implementationClass = "com.singularityindonesia.convention.features.FeatureContextReceiver"
        }
    }
}
