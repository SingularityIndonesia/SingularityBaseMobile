enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs2") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// include all libraries
File(settingsDir, "./")
    .listFiles()
    ?.filter { it.isDirectory }
    ?.filterNot { it.name.contains("gradle") }
    ?.filterNot { it.name.contains("build") }
    ?.filterNot { it.name.contains(".") }
    ?.forEach { dir ->
        include(":${dir.name}")
    }