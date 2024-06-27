/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("CompileWasm")
}

kotlin {
    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {
            implementation(project(":regex"))
        }
        iosMain.dependencies {

        }
    }
}

android {
    namespace = "shared.validation"
}

task("testClasses")