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
    namespace = "shared.validation"
}

task("testClasses")