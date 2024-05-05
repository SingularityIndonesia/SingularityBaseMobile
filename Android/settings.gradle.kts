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
        includeBuild("../Plugin")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
    repositories {
        google()
        mavenCentral()
    }
}

include(":app")

// include all Project Library
File(settingsDir, "library")
    .listFiles()
    ?.filter { it.isDirectory }
    ?.filterNot { it.name.contains("build") }
    ?.filterNot { it.name.contains(".gradle") }
    ?.forEach { dir ->
        include(":library:${dir.name}")
    }
