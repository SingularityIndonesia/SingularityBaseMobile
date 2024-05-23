package plugin.convention.companion

import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties

val Project.env
    get() = run {
        Properties()
            .apply {
                load(
                    FileInputStream(
                        project.file("${project.projectDir}/environment.properties")
                    )
                )
            }
    }