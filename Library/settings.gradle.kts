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

include(":data")
include(":exception")
include(":model")
include(":serialization")
include(":webrepository")
include(":regex")
include(":validation")
include(":analytic")
include(":compose-app")
include(":screen")
include(":designsystem")
include(":dictionary")
include(":flow")
include(":main-context")
include(":std-extra")