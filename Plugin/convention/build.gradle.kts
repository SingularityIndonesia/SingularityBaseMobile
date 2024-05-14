/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization")
}

group = "plugin.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

// Define the build configuration
tasks.register("generateBuildConfig") {
    doLast {
        val outputFile = file("src/main/kotlin/generated/VersionCatalog.kt")
        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            """

            object VersionCatalog {
                val TARGET_SDK = ${libs.versions.android.targetSdk.get()}
                val COMPILE_SDK = ${libs.versions.android.compileSdk.get()}
                val MIN_SDK = ${libs.versions.android.minSdk.get()}

                // JAVA
                val JAVA_SOURCE_COMPAT = org.gradle.api.JavaVersion.VERSION_11
                val JAVA_TARGET_COMPAT = org.gradle.api.JavaVersion.VERSION_11
                val JVM_TARGET = "${libs.versions.jvm.target.get()}"

                // KOTLIN
                val KOTLIN_VERSION = "${libs.versions.kotlin.get()}"
                /*val KOTLIN_COMPILER_EXTENSION_VERSION = "1.5.10"*/

                // JUNIT
                val JUNIT_VERSION = "${libs.versions.junit.get()}"
            }
            """.trimIndent()
        )
    }
}

tasks.named("compileKotlin") {
    dependsOn("generateBuildConfig")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("com.android.tools.build:gradle:${libs.versions.agp.get()}")
    implementation(libs.kotlinx.serialization.json)
}

gradlePlugin {
    plugins {
        register("AppConventionV1") {
            id = "AppConventionV1"
            implementationClass = "plugin.convention.AppConventionV1"
        }
        register("LibraryConventionV1") {
            id = "LibraryConventionV1"
            implementationClass = "plugin.convention.LibraryConventionV1"
        }
        register("FeatureJetpackCompose") {
            id = "FeatureJetpackCompose"
            implementationClass = "plugin.convention.features.FeatureJetpackCompose"
        }
        register("FeatureContextReceiver") {
            id = "FeatureContextReceiver"
            implementationClass = "plugin.convention.features.FeatureContextReceiver"
        }
        register("ApiGenerator") {
            id = "ApiGenerator"
            implementationClass = "plugin.convention.ApiGenerator"
        }
        register("PostmanClientGenerator") {
            id = "PostmanClientGenerator"
            implementationClass = "plugin.convention.PostmanClientGenerator"
        }
    }
}
