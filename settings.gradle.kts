/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
dependencyResolutionManagement {
    versionCatalogs {
        create("libs2") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

includeBuild("Main")
includeBuild("System") {
    dependencySubstitution {
        // include all Library
        File(settingsDir, "System")
            .listFiles()
            ?.asSequence()
            ?.filter { it.isDirectory }
            ?.filterNot { it.name.contains("gradle") }
            ?.filterNot { it.name.contains("build") }
            ?.filterNot { it.name.contains("iosApp") }
            ?.filterNot { it.name.contains(".") }
            ?.toList()
            ?.forEach { dir ->
                substitute(module("system:${dir.name}")).using(project(":${dir.name}"))
            }
    }
}
includeBuild("Shared") {
    dependencySubstitution {
        // include all Library
        File(settingsDir, "Shared")
            .listFiles()
            ?.asSequence()
            ?.filter { it.isDirectory }
            ?.filterNot { it.name.contains("gradle") }
            ?.filterNot { it.name.contains("build") }
            ?.filterNot { it.name.contains("iosApp") }
            ?.filterNot { it.name.contains(".") }
            ?.toList()
            ?.forEach { dir ->
                substitute(module("shared:${dir.name}")).using(project(":${dir.name}"))
            }
    }
}