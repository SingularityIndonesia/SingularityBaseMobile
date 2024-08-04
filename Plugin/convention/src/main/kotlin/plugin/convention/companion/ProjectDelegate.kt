package plugin.convention.companion

import org.gradle.api.Project

fun Project.taskIsRunningTest() =
    gradle.startParameter.taskNames
        .any { it == "check" || it.startsWith("test") || it.contains("Test", true) }
