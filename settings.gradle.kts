/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
rootProject.name = "Singularity Indonesia"
includeBuild("Android")
includeBuild("Shared") {
    dependencySubstitution {
        // include all Library
        File(settingsDir, "Shared")
            .listFiles()
            ?.filter { it.isDirectory }
            ?.filterNot { it.name.contains("gradle") }
            ?.filterNot { it.name.contains("build") }
            ?.forEach { dir ->
                substitute(module("std:${dir.name}")).using(project(":${dir.name}"))
            }
    }
}