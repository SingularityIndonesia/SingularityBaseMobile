/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
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
            implementation("shared:common")
            implementation("shared:webrepository")

            implementation(project(":example:model"))
            implementation(project(":ex_apigenerator:data"))
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
    }
}


android {


    namespace = "main.example.data"
    dependencies {

    }
}

task("testClasses")
