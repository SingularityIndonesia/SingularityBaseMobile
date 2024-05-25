/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("FeatureHttpClient")
    id("FeatureSerialization")
    id("io.github.stefanusayudha.PostmanClientGenerator")
}

kotlin {
    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {
            implementation("system:core")
            implementation("shared:common")
            implementation("shared:webrepository")

            implementation(project(":ex_apigenerator:model"))
        }
        iosMain.dependencies {

        }
    }
}


android {
    namespace = "main.ex_apigenerator.data"
    dependencies {

    }
}

task("testClasses")
