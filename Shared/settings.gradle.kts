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
    ?.filterNot { it.name.contains(".") }
    ?.forEach { dir ->
        include(":${dir.name}")
    }