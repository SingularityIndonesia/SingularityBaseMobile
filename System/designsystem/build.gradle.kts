/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    id("LibraryConventionV1")
    id("FeatureJetpackCompose")
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
    namespace = "system.designsystem"
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

task("testClasses")