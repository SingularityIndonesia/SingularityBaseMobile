/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization")
}

group = "plugin.postman_client_generator"

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
    implementation(libs.kotlinx.serialization.json)
}

gradlePlugin {
    plugins {
        register("PostmanClientGenerator") {
            id = "PostmanClientGenerator"
            implementationClass = "plugin.postman_client_generator.PostmanClientGenerator"
        }
    }
}
