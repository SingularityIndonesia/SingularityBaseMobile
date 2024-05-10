import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("LibraryConventionV1")
}

kotlin {
    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {

        }
        iosMain.dependencies {

        }
    }
}

android {
    namespace = "com.singularity.regex"
}

task("testClasses")