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

// include all libraries
File(settingsDir, "./")
    .listFiles()
    ?.filter { it.isDirectory }
    ?.filterNot { it.name.contains("gradle") }
    ?.filterNot { it.name.contains("build") }
    ?.forEach { dir ->
        include(":${dir.name}")
    }