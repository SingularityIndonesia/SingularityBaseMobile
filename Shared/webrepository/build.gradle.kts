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

            implementation("system:core")
        }
        iosMain.dependencies {
            implementation(libs2.ktor.client.ios)
        }
    }
}

android {
    namespace = "com.singularity.webrepository"
}

task("testClasses")