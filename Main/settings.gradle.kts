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
