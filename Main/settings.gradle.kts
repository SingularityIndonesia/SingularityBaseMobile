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
        includeBuild("../System") {
            dependencySubstitution {
                // include all Library
                File(settingsDir, "../System")
                    .listFiles()
                    ?.asSequence()
                    ?.filter { it.isDirectory }
                    ?.filter { it.listFiles()?.map { it.name }?.contains("build.gradle.kts") == true }
                    ?.onEach { dir ->
                        substitute(module("system:${dir.name}")).using(project(":${dir.name}"))
                    }
                    ?.toList()

            }
        }
        includeBuild("../Shared") {
            dependencySubstitution {
                // include all Library
                File(settingsDir, "../Shared")
                    .listFiles()
                    ?.asSequence()
                    ?.filter { it.isDirectory }
                    ?.filter { it.listFiles()?.map { it.name }?.contains("build.gradle.kts") == true }
                    ?.onEach { dir ->
                        substitute(module("shared:${dir.name}")).using(project(":${dir.name}"))
                    }
                    ?.toList()
            }
        }
    }
}

File(settingsDir, "./")
    .listFiles()
    ?.asSequence()
    ?.filter { it.isDirectory }
    ?.filter { it.listFiles()?.map { it.name }?.contains("build.gradle.kts") == true }
    ?.onEach { dir ->
        include(":${dir.name}")
    }
    ?.toList()


File(settingsDir, "./example")
    .listFiles()
    ?.asSequence()
    ?.filter { it.isDirectory }
    ?.filter { it.listFiles()?.map { it.name }?.contains("build.gradle.kts") == true }
    ?.onEach { dir ->
        include(":example:${dir.name}")
    }
    ?.toList()


File(settingsDir, "./ai_chat")
    .listFiles()
    ?.asSequence()
    ?.filter { it.isDirectory }
    ?.filter { it.listFiles()?.map { it.name }?.contains("build.gradle.kts") == true }
    ?.onEach { dir ->
        include(":ai_chat:${dir.name}")
    }
    ?.toList()


File(settingsDir, "./dashboard")
    .listFiles()
    ?.asSequence()
    ?.filter { it.isDirectory }
    ?.filter { it.listFiles()?.map { it.name }?.contains("build.gradle.kts") == true }
    ?.onEach { dir ->
        include(":dashboard:${dir.name}")
    }
    ?.toList()
