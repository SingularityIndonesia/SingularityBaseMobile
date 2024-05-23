/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization")
    id("com.gradle.plugin-publish") version "1.2.1"
}

version = "1.0.1"
group = "io.github.stefanusayudha"

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
    website.set("https://github.com/SingularityIndonesia/SingularityBaseMobile/")
    vcsUrl.set("https://github.com/SingularityIndonesia/SingularityBaseMobile/")
    plugins {
        register("PostmanClientGenerator") {
            id = "io.github.stefanusayudha.PostmanClientGenerator"
            implementationClass = "plugin.postman_client_generator.PostmanClientGenerator"
            displayName = "Postman Client Generator"
            description = "Plugin to generate postmant client automatically from postman collection."
            tags.set(listOf("kmm", "postman", "generator", "ktor", "webclient"))
        }
    }
}
