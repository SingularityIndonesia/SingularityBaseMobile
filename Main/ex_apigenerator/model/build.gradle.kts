plugins {
    id("LibraryConventionV1")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
        iosMain.dependencies {

        }
    }
}

android {
    namespace = "main.ex_apigenerator.model"
    dependencies {

    }
}

task("testClasses")
