plugins {
    id("AppConventionV1")
    id("FeatureJetpackCompose")
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
            implementation("system:core")
            implementation("system:designsystem")
            implementation("shared:webrepository")
        }
        iosMain.dependencies {

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