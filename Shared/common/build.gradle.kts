plugins {
    id("LibraryConventionV1")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs2.ktor.client.okhttp)
        }
        commonMain.dependencies {
            // ktor
            implementation(libs2.ktor.client.core)
            implementation(libs2.ktor.client.cio)

            implementation(libs2.kotlinx.serialization.json)
        }
        iosMain.dependencies {
            implementation(libs2.ktor.client.ios)
        }
    }
}

android {
    namespace = "shared.common"
}

task("testClasses")