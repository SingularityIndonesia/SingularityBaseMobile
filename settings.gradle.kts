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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Singularity Indonesia"
include(":Project:app")
include(":Project:library:debugger")
include(":Project:library:dictionary")
include(":Project:library:model")
include(":Project:library:exception")
include(":Project:library:designsystem")
include(":Project:library:webrepository")
include(":Project:library:main-context")
include(":Project:screen")
include(":Project:library:data")
include(":Project:library:analytics")
include(":Project:library:serialization")
