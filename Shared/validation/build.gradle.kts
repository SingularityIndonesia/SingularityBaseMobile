import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("LibraryConventionV1")
}

kotlin {
    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {
            implementation(project(":regex"))
        }
        iosMain.dependencies {

        }
    }
}

android {
    namespace = "com.singularity.validation"
}

task("testClasses")