plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.json)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
    }
}

android {
    namespace = "shared.common"
}

task("testClasses")