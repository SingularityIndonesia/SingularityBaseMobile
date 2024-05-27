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
                    ?.filterNot { it.name.contains("gradle") }
                    ?.filterNot { it.name.contains("build") }
                    ?.filterNot { it.name.contains("iosApp") }
                    ?.filterNot { it.name.contains(".") }
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
                    ?.filterNot { it.name.contains("gradle") }
                    ?.filterNot { it.name.contains("build") }
                    ?.filterNot { it.name.contains("iosApp") }
                    ?.filterNot { it.name.contains(".") }
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
    ?.filterNot { it.name.contains("gradle") }
    ?.filterNot { it.name.contains("build") }
    ?.filterNot { it.name.contains(".") }
    ?.toList()
    ?.forEach { dir ->
        include(":${dir.name}")
    }


File(settingsDir, "./example")
    .listFiles()
    ?.asSequence()
    ?.filter { it.isDirectory }
    ?.filterNot { it.name.contains("gradle") }
    ?.filterNot { it.name.contains("build") }
    ?.filterNot { it.name.contains(".") }
    ?.toList()
    ?.forEach { dir ->
        include(":example:${dir.name}")
    }


File(settingsDir, "./ex_apigenerator")
    .listFiles()
    ?.asSequence()
    ?.filter { it.isDirectory }
    ?.filterNot { it.name.contains("gradle") }
    ?.filterNot { it.name.contains("build") }
    ?.filterNot { it.name.contains(".") }
    ?.toList()
    ?.forEach { dir ->
        include(":ex_apigenerator:${dir.name}")
    }
