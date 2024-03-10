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
        includeBuild("plugin")
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
include(":app")
include(":library:debugger")
include(":library:dictionary")
include(":library:model")
include(":library:exception")
include(":library:designsystem")
include(":library:webrepository")
include(":library:main-context")
include(":screen")
include(":library:data")
include(":library:analytics")
include(":library:serialization")
