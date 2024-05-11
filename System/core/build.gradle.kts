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
        }
        iosMain.dependencies {
            implementation(libs2.ktor.client.ios)
        }
    }
}

android {
    namespace = "system.core"
}

task("testClasses")