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
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
    }
}

android {
    namespace = "system.core"
}

task("testClasses")