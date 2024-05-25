plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
}

kotlin {
    sourceSets {
        
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            // ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
    }
}

android {
    namespace = "system.core"
}

task("testClasses")