/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("FeatureKtor")
    id("FeatureSerialization")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("system:core")
            implementation("shared:common")
            implementation("shared:webrepository")

            implementation(project(":example:model"))
            implementation(project(":ex_apigenerator:data"))
        }
    }
}


android {
    namespace = "main.example.data"
}

task("testClasses")
