/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
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