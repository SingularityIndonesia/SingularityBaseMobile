import com.singularityindonesia.convention.companion.kotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("AppConventionV1")
    id("FeatureJetpackCompose")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
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

            implementation("system:core")
            implementation("system:designsystem")
            implementation("shared:common")
            implementation("shared:webrepository")

            implementation(libs.kotlinx.serialization.json)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
    }
}

android {
    namespace = "com.singularity.basemobile"
    defaultConfig {
        applicationId = "com.singularity.basemobile"
        versionCode = 1
        versionName = "1.0"
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

task("testClasses")