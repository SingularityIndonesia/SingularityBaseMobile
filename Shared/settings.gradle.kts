/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
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
        includeBuild("../Plugin")
    }
}

dependencyResolutionManagement {
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
    ?.asSequence()
    ?.filter { it.isDirectory }
    ?.filterNot { it.name.contains("gradle") }
    ?.filterNot { it.name.contains("build") }
    ?.filterNot { it.name.contains(".") }
    ?.toList()
    ?.forEach { dir ->
        include(":${dir.name}")
    }