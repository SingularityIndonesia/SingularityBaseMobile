import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("LibraryConventionV1")
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    sourceSets {
        
        androidMain.dependencies {
            implementation(libs2.compose.ui.tooling.preview)
            implementation(libs2.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

android {
    namespace = "com.singularity.designsystem"
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

task("testClasses")