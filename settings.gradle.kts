/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        includeBuild("Plugin")
    }
}
dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        includeBuild("Library") {
            dependencySubstitution {
                // include all Library
                File(settingsDir, "Library")
                    .listFiles()
                    ?.filter { it.isDirectory }
                    ?.filterNot { it.name.contains("gradle") }
                    ?.filterNot { it.name.contains("build") }
                    ?.forEach { dir ->
                        substitute(module("${dir.name}:main")).using(project(":${dir.name}"))
                    }
            }
        }
    }

}

rootProject.name = "Singularity Indonesia"

include(":Project:Android:app")

// include all Project Library
File(settingsDir, "Project/Android/library")
    .listFiles()
    ?.filter { it.isDirectory }
    ?.filterNot { it.name.contains("build") }
    ?.filterNot { it.name.contains(".gradle") }
    ?.forEach { dir ->
        include(":Project:Android:library:${dir.name}")
    }
