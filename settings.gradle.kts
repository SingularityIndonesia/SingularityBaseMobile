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
        includeBuild("Library") {
            dependencySubstitution {
                substitute(module("exception:main-SNAPSHOT")).using(project(":exception"))
                substitute(module("webrepository:main-SNAPSHOT")).using(project(":webrepository"))
                substitute(module("compose-app:main-SNAPSHOT")).using(project(":compose-app"))
                substitute(module("analytic:main-SNAPSHOT")).using(project(":analytic"))
                substitute(module("main-context:main-SNAPSHOT")).using(project(":main-context"))
                substitute(module("dictionary:main-SNAPSHOT")).using(project(":dictionary"))          }

        }
    }
}

rootProject.name = "Singularity Indonesia"

include(":Project:Android:app")
include(":Project:Android:library:debugger")
