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
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit)
        }
        iosMain.dependencies {

        }
    }
}

android {
    namespace = "com.singularity.regex"
}

task("testClasses")