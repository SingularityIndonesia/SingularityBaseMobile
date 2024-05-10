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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("com.android.tools.build:gradle:${libs.versions.agp.get()}")
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
