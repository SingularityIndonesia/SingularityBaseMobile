plugins {
    id("LibraryConventionV1")
    id("ApiGenerator")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)

            implementation(libs.kotlinx.serialization.json)

            implementation("system:core")
            implementation("shared:common")
            implementation("shared:webrepository")

            implementation(project(":example:model"))
        }
        iosMain.dependencies {
          implementation(libs.ktor.client.ios)
        }
    }
}


android {


    namespace = "main.example.data"
    dependencies {

    }
}

task("testClasses")
