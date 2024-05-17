plugins {
    id("LibraryConventionV1")
    id("FeatureJetpackCompose")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)

            implementation(libs.kotlinx.serialization.json)

            implementation("system:core")
            implementation("system:designsystem")
            implementation("shared:common")

            implementation(project(":ex_apigenerator:data"))
            implementation(project(":ex_apigenerator:model"))
        }
        iosMain.dependencies {
          implementation(libs.ktor.client.ios)
        }
    }
}

android {


    namespace = "main.ex_apigenerator.presentation"
    dependencies {
      debugImplementation(libs.compose.ui.tooling)
    }
}

task("testClasses")
