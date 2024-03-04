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
include(":library:extension:debugger")
include(":library:extension:analytic")
include(":library:dictionary")
include(":library:model")
include(":library:exception")
include(":library:designsystem")
include(":library:webrepository")
